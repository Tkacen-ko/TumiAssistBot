package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class MessageBuilder {

    private MessageUtils messageUtils;
    private ReactionToMessages reactionToMessages;

    @Autowired
    public MessageBuilder(MessageUtils messageUtils,
                          ReactionToMessages reactionToMessages)
    {
        this.messageUtils = messageUtils;
        this.reactionToMessages = reactionToMessages;
    }
    public String basicAnswerer(Message message)
    {
        User user = messageUtils.userCheck(message);
        messageUtils.saveMessage(user, message);

        return reactionToMessages.answerSelection(message.getText());
    }
}
