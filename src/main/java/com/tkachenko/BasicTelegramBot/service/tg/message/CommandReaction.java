package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.dto.BasicTelegramData;
import com.tkachenko.BasicTelegramBot.model.finance.Account;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Map;

@Service
public class CommandReaction {

    private UserRepository userRepository;
    private AccountBuilder accountBuilder;

    public CommandReaction(UserRepository userRepository, AccountBuilder accountBuilder)
    {
        this.userRepository = userRepository;
        this.accountBuilder = accountBuilder;
    }

    SendMessage commandProcessing(BasicTelegramData basicTelegramData,
                             String command, Map<String,
                             Account> temporaryAccounts,
                             SendMessage answererMessage)
    {
        if (command.equals("create_account"))
        {
            Account account = new Account();
            account.setUser(userRepository.findByChatId(Long.parseLong(basicTelegramData.getIdChat())).get());
            temporaryAccounts.put(basicTelegramData.getIdChat(), account);

            InlineKeyboardMarkup keyboardMarkup = accountBuilder.fillingAccount(basicTelegramData ,account, answererMessage);
            answererMessage.setReplyMarkup(keyboardMarkup);
        }

        return answererMessage;
    }
}
