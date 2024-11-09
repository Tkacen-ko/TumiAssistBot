package com.tkachenko.BasicTelegramBot.config;

import com.tkachenko.BasicTelegramBot.service.tg.MainTgBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(MainTgBot mainTgBot) {
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(mainTgBot); // Регистрация бота
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return botsApi;
    }
}