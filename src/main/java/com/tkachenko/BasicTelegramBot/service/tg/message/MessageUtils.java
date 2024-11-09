package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.MessageTG;
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

    public User userCheck(Message message) {
        return userRepository.findByChatIdAndUserName(message.getChatId(),
                        message.getFrom().getUserName()).orElseGet(() -> userRepository.save(
                        new User(message.getChatId(),
                                message.getFrom().getFirstName(),
                                message.getFrom().getLastName(),
                                message.getFrom().getUserName())
                ));
    }

    public void saveMessage(User user, Message message)
    {
        // Сохраняем сообщение
        MessageTG savedMessageTG = new MessageTG();
        savedMessageTG.setUser(user);
        savedMessageTG.setText(message.getText());
        savedMessageTG.setTimestamp(LocalDateTime.now());
        messageRepository.save(savedMessageTG);
    }
}
