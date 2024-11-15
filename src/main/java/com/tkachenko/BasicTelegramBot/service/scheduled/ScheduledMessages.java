package com.tkachenko.BasicTelegramBot.service.scheduled;

import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.service.tg.MainTgBot;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@EnableScheduling
public class ScheduledMessages {
    private final static String GOOD_MORNING = "Доброе утро! Это ежедневное сообщение.";

    private UserRepository userRepository;
    private MainTgBot mainTgBot;

    @Autowired
    public ScheduledMessages(UserRepository userRepository,
                             MainTgBot mainTgBot)
    {
        this.userRepository = userRepository;
        this.mainTgBot = mainTgBot;
    }

    // Ежедневное уведомление пользователю
    @Scheduled(cron = "0 0 9 * * ?")// каждый день в 9 утра
    public void sendDailyMessage() {
        userRepository.findAll().forEach(user ->
                mainTgBot.sendMessage(new SendMessage(user.getChatId().toString(), GOOD_MORNING))
        );
    }
}
