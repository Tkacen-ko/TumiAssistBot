package com.tkachenko.BasicTelegramBot.service.tg.respondent;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.ThoughtUser;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CommonService {

    private final ButtonsBuilder buttonsBuilder;

    CommonService(ButtonsBuilder buttonsBuilder)
    {
        this.buttonsBuilder = buttonsBuilder;
    }

    void replyUnknownMessage(SendMessage sendMessage)
    {
        Map<String, String> buttonData = new LinkedHashMap<>();
        buttonData.putAll(ButtonConstant.UNKNOWN_MESSAGE_FROM_USER);
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage.setText(StringConstant.REACTION_UNKNOWN_MESSAGE);
    }
}
