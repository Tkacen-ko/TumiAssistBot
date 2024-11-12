package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.Account;
import com.tkachenko.BasicTelegramBot.model.finance.GeneralData;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReactionToMessages {
    Map<Long, Account> temporaryAccounts = new HashMap<>();
    Command command;

    @Autowired
    public ReactionToMessages(Command command)
    {
        this.command = command;
    }

    String answerSelection(UserTelegram user, Message message)
    {
        String answerText = ConstantTgBot.BASIC_MASSAGE;
        String textMessage = message.getText();

        if(textMessage.matches(ConstantTgBot.PATTERN_BUDGET_MESSAGE))
        {
            answerText = "Сообщение бюджетного характера";
        }
        else if(temporaryAccounts.get(user.getChatId()) != null)
        {
            answerText = "Вы не завершили процесс заполнения формы, вы уверены что это ок?";
        }
        else if(textMessage.charAt(0) == '/')
        {
            answerText = command.commandProcessing(user, textMessage.substring(1), temporaryAccounts);
        }

        return answerText;
    }


}
