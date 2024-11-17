package com.tkachenko.BasicTelegramBot.service.tg;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.MessagesHistoryData;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.utils.MessageUtils;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.MainTGController;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;


@Service
public class MainTgBot extends TelegramLongPollingBot {
    /**
     * Основаня переменнная для промежуточного хранения данных между запросами из бота
     */
    private final Map<String, BasicInformationMessage> basicInformationMessage = new HashMap<>();

    @Value("${telegram.bot.username}")
    private String usernameBot;
    @Value("${telegram.bot.token}")
    private String tokenBot;

    private final MessageUtils messageUtils;
    private final MainTGController reactionToMessages;

    @Autowired
    public MainTgBot(MessageUtils messageUtils,
                     MainTGController reactionToMessages)
    {
        this.messageUtils = messageUtils;
        this.reactionToMessages = reactionToMessages;
    }

    @Override
    public String getBotUsername() {
        return usernameBot;
    }

    @Override
    public String getBotToken() {
        return tokenBot;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage answererMessage = new SendMessage();

        try {
            BasicInformationMessage basicTelegramData = updateBasicData(update, answererMessage);
            answererMessage = reactionToMessages.answerSelection(basicTelegramData, answererMessage);
            savHistorySentMessages(answererMessage);
        } catch (Exception e) {
            answererMessage = MessageUtils.createSimpleMessage(answererMessage.getChatId(), ConstantTgBot.ERROR_MESSAGE);
            e.printStackTrace();
        }

        sendMessage(answererMessage);
    }

    public void sendMessage(SendMessage answererMessage) {
        try {
            execute(answererMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getChatId(Update update) {
        Long chatId = null;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        return chatId.toString();
    }

    private String getTextMessage(Update update) {
        String textMessage = null;
        if (update.hasMessage()) {
            textMessage = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            textMessage = update.getCallbackQuery().getData();
        }

        return textMessage;
    }

    private Integer getMessageId(Update update) {
        if (update.hasMessage() && update.getMessage() != null) {
            return update.getMessage().getMessageId();
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return null;
    }

    public Message getMessageFromUpdate(Update update) {
        if (update.hasMessage()) {
            return update.getMessage();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage();
        }
        // Если ни сообщения, ни реакции на кнопку нет
        return null;
    }

    private BasicInformationMessage updateBasicData(Update update, SendMessage answererMessage)
    {
        String chatId = getChatId(update);
        String textMessage = getTextMessage(update);
        Message messageFromUpdate = getMessageFromUpdate(update);
        UserTelegram userTelegram = messageUtils.userCheck(messageFromUpdate);

        BasicInformationMessage basicTelegramData = basicInformationMessage.get(userTelegram.getChatId().toString());
        basicTelegramData = messageUtils.checkBasicInformation(basicTelegramData, userTelegram, textMessage, basicInformationMessage);

        String messageId = getMessageId(update).toString();
        MessagesHistoryData previousMessages = new MessagesHistoryData(messageId ,textMessage);
        basicTelegramData.getHistoryReceivedMessages().add(previousMessages);
        answererMessage.setChatId(chatId);

        return basicTelegramData;
    }

    private void savHistorySentMessages(SendMessage answererMessage)
    {
        MessagesHistoryData messagesHistoryData = new MessagesHistoryData(answererMessage.getChatId().toString(), answererMessage.getText());
        basicInformationMessage.get(answererMessage.getChatId()).getHistorySentMessages().add(messagesHistoryData);
    }
}