package com.tkachenko.BasicTelegramBot.service.tg.respondent;

import java.util.*;
import java.util.stream.Stream;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonReaction;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.CommandReaction;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.intermediateData.IntermediateProcessing;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MainTGController {
    /**
     * Основаня переменнная для промежуточного хранения данных между запросами из бота
     */
    private final Map<String, Intermediate> intermediateData = new HashMap<>();

    private final CommandReaction commandReaction;
    private final ButtonReaction buttonReaction;
    private final IntermediateProcessing intermediate;

    @Autowired
    public MainTGController(CommandReaction commandReaction,
                            ButtonReaction buttonReaction,
                            IntermediateProcessing intermediate)
    {
        this.commandReaction = commandReaction;
        this.buttonReaction = buttonReaction;
        this.intermediate = intermediate;
    }

    /**
     * Метод выбора ответа на сообщение пользователя
     * @param basicInformationMessage - базовые информация сообщения
     * @param sendMessage - сообщение для отправки пользователю
     * @return сообщение для пользователя
     */
    public SendMessage answerSelection(BasicInformationMessage basicInformationMessage,
                                       SendMessage sendMessage)
    {
        String textMessage = basicInformationMessage.getMessageText();
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        intermediateData.putIfAbsent(chatId, new Intermediate());

        if(intermediateData.get(chatId).checkData())
        {
            intermediate.checkingForIntermediateData(basicInformationMessage, sendMessage, intermediateData);
        }

        if(sendMessage.getText() == null && textMessage.charAt(0) == '@')
        {
            buttonReaction.buttonProcessing(basicInformationMessage, sendMessage, intermediateData);
        }

        if(sendMessage.getText() == null && textMessage.charAt(0) == '/')
        {
            commandReaction.commandProcessing(basicInformationMessage, sendMessage, intermediateData);
        }

        if(sendMessage.getText() == null)
        {
            sendMessage.setText(ConstantTgBot.BASIC_MESSAGE);
        }


        return sendMessage;
    }
}
