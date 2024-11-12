package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageUtils {

    private UserRepository userRepository;

    @Autowired
    public MessageUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserTelegram userCheck(Message message) {
        return userRepository.findByChatIdAndUserName(message.getChatId(),
                        message.getFrom().getUserName()).orElseGet(() -> userRepository.save(
                        new UserTelegram(message.getChatId(),
                                message.getFrom().getFirstName(),
                                message.getFrom().getLastName(),
                                message.getFrom().getUserName())
                ));
    }
}
