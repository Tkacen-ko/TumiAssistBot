package com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ButtonsBuilder {

    public InlineKeyboardMarkup createMessageWithButtons(Map<String, String> buttonData) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttonData.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton(entry.getKey());
            button.setCallbackData(entry.getValue());
            rows.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }
}
