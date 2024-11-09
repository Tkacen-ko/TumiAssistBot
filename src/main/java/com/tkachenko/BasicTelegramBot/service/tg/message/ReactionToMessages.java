package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.Message;
import com.tkachenko.BasicTelegramBot.repository.MessageRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ReactionToMessages {
    private MessageRepository messageRepository;

    @Autowired
    public ReactionToMessages(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    String answerSelection(String textMessage)
    {
        String answerText = ConstantTgBot.BASIC_MASSAGE;
        if (textMessage.equals("/last10")) {
            answerText = getLastMessages();
        }

        return answerText;
    }

    private String getLastMessages() {
        List<Message> messages = messageRepository.findTop10ByOrderByTimestampDesc();
        StringBuilder sb = new StringBuilder("Последние 10 сообщений:\n");
        messages.forEach(msg -> sb.append(msg.getUser().getUsername())
                .append(": ")
                .append(msg.getText())
                .append("\n"));

        return sb.toString();
    }
}
