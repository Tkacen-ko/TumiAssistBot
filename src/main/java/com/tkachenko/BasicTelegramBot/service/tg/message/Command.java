package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.Account;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Command {

    String commandProcessing(UserTelegram user, String command, Map<Long, Account> temporaryAccounts)
    {
        if (command.equals("create_account"))
        {
            Account account = new Account();
            account.setUser(user);
            temporaryAccounts.put(user.getChatId(), account);
            return "От меня хотят создания нового счёта";
        }
        return "Неизвестная команда";
    }
}
