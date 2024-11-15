package com.tkachenko.BasicTelegramBot.service.tg.message;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
            rows.add(Collections.singletonList(button)); // Каждая кнопка на своей строке
        }

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }
}
