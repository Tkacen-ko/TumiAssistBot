package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.MessageTG;
import com.tkachenko.BasicTelegramBot.model.User;
import com.tkachenko.BasicTelegramBot.repository.MessageRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class ReactionToMessages {

    private MessageRepository messageRepository;

    @Autowired
    public ReactionToMessages(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    String answerSelection(User user, Message message)
    {
        String answerText = ConstantTgBot.BASIC_MASSAGE;

        if(message.getText().matches(ConstantTgBot.PATTERN_BUDGET_MESSAGE))
        {
            answerText = "Сообщение бюджетного характера";
        }

        return answerText;
    }


}
