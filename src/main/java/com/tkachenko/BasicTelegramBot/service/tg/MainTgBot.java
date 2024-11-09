package com.tkachenko.BasicTelegramBot.service.tg;


import com.tkachenko.BasicTelegramBot.service.tg.message.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class MainTgBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String usernameBot;
    @Value("${telegram.bot.token}")
    private String tokenBot;

    private MessageBuilder messageBuilder;

    @Autowired
    public MainTgBot(MessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
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
        String answerer;

        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                answerer =  messageBuilder.basicAnswerer(update.getMessage());
            } catch (Exception e) {
                answerer = ConstantTgBot.ERROR_MASSAGE;
                e.printStackTrace();
            }
            sendMessage(update.getMessage().getChatId(), answerer);
        }
    }

    public void sendMessage(Long chatId, String text) {
        try {
            execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}