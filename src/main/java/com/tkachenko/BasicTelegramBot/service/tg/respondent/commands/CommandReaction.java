package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.CommandConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class CommandReaction {

    private final ButtonsBuilder buttonsBuilder;

    @Autowired
    CommandReaction(ButtonsBuilder buttonsBuilder)
    {
        this.buttonsBuilder = buttonsBuilder;
    }
    public SendMessage commandProcessing(BasicInformationMessage basicInformationMessage,
                                         SendMessage sendMessage,
                                         Map<String, Intermediate> intermediateData)
    {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        String textMessage = basicInformationMessage.getMessageText();

        if(textMessage.equals(CommandConstant.START_COMMAND))
        {
            sendMessage.setText(StringConstant.START_MESSAGE);

            Map<String, String> buttonData = new LinkedHashMap<>();
            buttonData.putAll(ButtonConstant.START_BLOCK);
            buttonData.putAll(ButtonConstant.CANCEL_CREATION);
            sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(buttonData));

            return sendMessage;
        }
        else if(textMessage.equals(CommandConstant.START_COMMAND))
        {
            sendMessage.setText(StringConstant.START_MESSAGE);

            Map<String, String> buttonData = new LinkedHashMap<>();
            buttonData.putAll(ButtonConstant.START_BLOCK);
            buttonData.putAll(ButtonConstant.CANCEL_CREATION);
            sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(buttonData));

            return sendMessage;
        }
        return sendMessage;
    }
}
