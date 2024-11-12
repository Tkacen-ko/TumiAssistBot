package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

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
        UserTelegram user = messageUtils.userCheck(message);
        return reactionToMessages.answerSelection(user, message);
    }
}
