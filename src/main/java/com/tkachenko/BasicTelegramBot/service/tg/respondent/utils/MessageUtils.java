package com.tkachenko.BasicTelegramBot.service.tg.respondent.utils;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Service
public class MessageUtils {

    private UserRepository userRepository;

    @Autowired
    public MessageUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserTelegram userCheck(Message message) {
        if (message == null) {
            return null;
        }
        Long chatId = message.getChatId();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        String userName = message.getFrom().getUserName();

        UserTelegram userTelegram = userRepository.findByChatId(chatId).orElseGet(() ->
                userRepository.save(new UserTelegram(chatId, firstName, lastName, userName)));

        return userTelegram;
    }

    public BasicInformationMessage checkBasicInformation(BasicInformationMessage basicTelegramData,
                                                         UserTelegram userTelegram,
                                                         String textMessage,
                                                         Map<String, BasicInformationMessage> basicInformationMessage) {
        if(basicTelegramData == null)
        {
            BasicInformationMessage newBasicData = new BasicInformationMessage(userTelegram,
                    textMessage,
                    new LimitedSizeMessageList<>(10),
                    new LimitedSizeMessageList<>(10));
            basicInformationMessage.put(userTelegram.getChatId().toString(), newBasicData);

            return newBasicData;
        }
        else
        {
            basicTelegramData.setMessageText(textMessage);
            return basicTelegramData;
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
