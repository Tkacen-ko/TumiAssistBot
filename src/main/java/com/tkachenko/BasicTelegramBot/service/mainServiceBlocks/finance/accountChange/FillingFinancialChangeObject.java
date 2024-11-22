package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.accountChange;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.expenses.ExpenseType;
import com.tkachenko.BasicTelegramBot.model.finance.expenses.FinancialChange;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.expenses.ExpenseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.Optional;

@Service
public class FillingFinancialChangeObject {

    private final UserRepository userRepository;
    private final ExpenseTypeRepository expenseTypeRepository;

    @Autowired
    FillingFinancialChangeObject(UserRepository userRepository,
                                 ExpenseTypeRepository expenseTypeRepository)
    {
        this.userRepository = userRepository;
        this.expenseTypeRepository = expenseTypeRepository;
    }

    FinancialChange fillingFinancialChange(BasicInformationMessage basicInformationMessage,
                                           SendMessage sendMessage,
                                           Map<String, Intermediate> intermediateData)
    {
        String textMessage = basicInformationMessage.getMessageText();
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        FinancialChange financialChange = new FinancialChange();
        String[] balanceChangeData = null;
        int balanceChangeDataSize = 0;

        if (!textMessage.contains(" ")) {
            financialChange = fillingAmount(textMessage, sendMessage);
            if(sendMessage.getText() != null)
            {
                return financialChange;
            }
        }
        else
        {
            balanceChangeData = textMessage.split(" ");

            balanceChangeDataSize = balanceChangeData.length;
            if(balanceChangeDataSize > 3)
            {
                sendMessage.setText(ConstantAccountChange.ERROR_CHANGING_ACCOUNT_BALANCE);
                return financialChange;
            }
        }
        financialChange = fillingAmount(textMessage, sendMessage);
        if(sendMessage.getText() != null)
        {
            return financialChange;
        }
        intermediateData.get(chatId).setFinancialChange(new FinancialChange());
        intermediateData.get(chatId).getFinancialChange().setAmount(financialChange.getAmount());

        Optional<UserTelegram> user = userRepository.findByChatId(Long.parseLong(chatId));
        intermediateData.get(chatId.toString()).getFinancialChange().setUser(user.get());

        String shortNameCompany = null;
        String typeExpenses = null;

        if(balanceChangeDataSize == 2)
        {
            shortNameCompany = balanceChangeData[1];
            fillingFinancialAccount(basicInformationMessage,
                    shortNameCompany,
                    financialChange,
                    intermediateData
            );
        }
        else if(balanceChangeDataSize == 3)
        {
            shortNameCompany = balanceChangeData[1];
            typeExpenses = balanceChangeData[2];
            fillingFinancialAccount(basicInformationMessage,
                    shortNameCompany,
                    financialChange,
                    intermediateData
            );
            fillingExpenseType(basicInformationMessage,
                    typeExpenses,
                    financialChange,
                    intermediateData
            );
        }

        return financialChange;
    }

    private double getNumberChangesAccount(String textMessage,
                                           SendMessage sendMessage)
    {
        double numberChangesAccount = 0.0;
        try {
            numberChangesAccount = Double.parseDouble(textMessage);
        }
        catch (NumberFormatException e)
        {
            sendMessage.setText(ConstantAccountChange.ERROR_CHANGING_ACCOUNT_BALANCE);
        }

        return numberChangesAccount;
    }

    private FinancialChange fillingAmount(String textMessage,
                                          SendMessage sendMessage)
    {
        FinancialChange financialChange = new FinancialChange();
        double amount = getNumberChangesAccount(textMessage, sendMessage);
        financialChange.setAmount(amount);

        return financialChange;
    }

    private void fillingFinancialAccount(BasicInformationMessage basicInformationMessage,
                                         String shortNameCompany,
                                         FinancialChange financialChange,
                                         Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        Optional<UserTelegram> user = userRepository.findByChatId(chatId);

        Optional<FinancialAccount> financeAccount =  user.get().getFinancialAccounts().stream()
                .filter(org -> org.getFinancialOrganization().getShortName().equals(shortNameCompany))
                .findFirst();
        if(!financeAccount.isEmpty())
        {
            financialChange.setFinancialAccount(financeAccount.get());
            intermediateData.get(chatId.toString()).getFinancialChange().setFinancialAccount(financeAccount.get());
        }
    }

    private void fillingExpenseType(BasicInformationMessage basicInformationMessage,
                                    String typeExpenses,
                                    FinancialChange financialChange,
                                    Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        Optional<ExpenseType> expenseType = expenseTypeRepository.findByShortName(typeExpenses);

        if(!expenseType.isEmpty())
        {
            financialChange.setExpenseType(expenseType.get());
            intermediateData.get(chatId.toString()).getFinancialChange().setExpenseType(expenseType.get());
        }
    }
}
