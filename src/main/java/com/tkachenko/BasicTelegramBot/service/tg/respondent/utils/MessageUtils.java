package com.tkachenko.BasicTelegramBot.service.tg.respondent.utils;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
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

    public UserTelegram userCheck(Update update) {
        if (!update.hasMessage()) {
            return null;
        }

        Message message = update.getMessage();
        UserTelegram userTelegram = userRepository.findByChatIdAndUserName(message.getChatId(),
                        message.getFrom().getUserName()).orElseGet(() -> userRepository.save(
                        new UserTelegram(message.getChatId(),
                                message.getFrom().getFirstName(),
                                message.getFrom().getLastName(),
                                message.getFrom().getUserName())
                ));

        return userTelegram;
    }

    public BasicInformationMessage checkBasicInformation(BasicInformationMessage basicInformationMessage,
                                      UserTelegram userTelegram,
                                      String textMessage)
    {
        if(basicInformationMessage == null)
        {
            return new BasicInformationMessage(userTelegram, textMessage, new LimitedSizeMessageList<>(10));
        }
        else
        {
            return basicInformationMessage;
        }
    }

    public static SendMessage createSimpleMessage(String setChatId, String textMessage)
    {
        SendMessage message = new SendMessage();
        message.setChatId(setChatId);
        message.setText(textMessage);

        return message;
    }
}
