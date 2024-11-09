package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.User;
import com.tkachenko.BasicTelegramBot.repository.MessageRepository;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;

@Service
public class MessageUtils {

    private UserRepository userRepository;
    private MessageRepository messageRepository;

    @Autowired
    public MessageUtils(UserRepository userRepository,
                        MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public User userCheck(Message message)
    {
        return userRepository.findById(message.getChatId()).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(message.getChatId());
            newUser.setFirstName(message.getFrom().getFirstName());
            newUser.setLastName(message.getFrom().getLastName());
            newUser.setUsername(message.getFrom().getUserName());
            return userRepository.save(newUser);
        });
    }

    public void saveMessage(User user, Message message)
    {
        // Сохраняем сообщение
        com.tkachenko.BasicTelegramBot.model.Message savedMessage = new com.tkachenko.BasicTelegramBot.model.Message();
        savedMessage.setUser(user);
        savedMessage.setText(message.getText());
        savedMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(savedMessage);
    }
}
