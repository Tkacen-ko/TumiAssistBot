package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageUtils {

    private UserRepository userRepository;

    @Autowired
    public MessageUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void userCheck(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        Message message = update.getMessage();
        userRepository.findByChatIdAndUserName(message.getChatId(),
                        message.getFrom().getUserName()).orElseGet(() -> userRepository.save(
                        new UserTelegram(message.getChatId(),
                                message.getFrom().getFirstName(),
                                message.getFrom().getLastName(),
                                message.getFrom().getUserName())
                ));
    }

    public static SendMessage createSimpleMessage(String setChatId, String textMessage)
    {
        SendMessage message = new SendMessage();
        message.setChatId(setChatId);
        message.setText(textMessage);

        return message;
    }
}
