package com.tkachenko.BasicTelegramBot.service.tg;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tkachenko.BasicTelegramBot.dto.BasicTelegramData;
import com.tkachenko.BasicTelegramBot.service.tg.message.MessageUtils;
import com.tkachenko.BasicTelegramBot.service.tg.message.ReactionToMessages;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
public class MainTgBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String usernameBot;
    @Value("${telegram.bot.token}")
    private String tokenBot;

    private final MessageUtils messageUtils;
    private final ReactionToMessages reactionToMessages;

    @Autowired
    public MainTgBot(MessageUtils messageUtils,
                     ReactionToMessages reactionToMessages)
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
        String chatId = getChatId(update);

        try {
            String textMessage = getTextMessage(update);
            BasicTelegramData basicTelegramData = new BasicTelegramData(chatId, textMessage);
            answererMessage.setChatId(chatId);
            messageUtils.userCheck(update);

            answererMessage = reactionToMessages.answerSelection(basicTelegramData, answererMessage);

        } catch (Exception e) {
            answererMessage = MessageUtils.createSimpleMessage(chatId, ConstantTgBot.ERROR_MASSAGE);
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
}