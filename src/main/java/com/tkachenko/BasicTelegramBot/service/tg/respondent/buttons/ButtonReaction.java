package com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.expenses.ExpenseType;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.expenses.ExpenseTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.general.CurrencyRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.accountChange.ConstantAccountChange;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.accountChange.FillingAccountChange;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.addAccount.FillingFinancialAccount;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.CommandConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.TitleButtonConstant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ButtonReaction {

    private final CurrencyRepository currencyRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final FillingFinancialAccount financialAccountFilling;
    private final UserRepository userRepository;
    private final FillingAccountChange fillingAccountChange;
    private final ExpenseTypeRepository expenseTypeRepository;

    ButtonReaction(CurrencyRepository currencyRepository,
                   AccountTypeRepository accountTypeRepository,
                   FillingFinancialAccount financialAccountFilling,
                   UserRepository userRepository,
                   FillingAccountChange fillingAccountChange,
                   ExpenseTypeRepository expenseTypeRepository)
    {
        this.currencyRepository = currencyRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.financialAccountFilling = financialAccountFilling;
        this.userRepository = userRepository;
        this.fillingAccountChange = fillingAccountChange;
        this.expenseTypeRepository = expenseTypeRepository;
    }

    /**
     * Метод реакции на нажатие кнопки пользователем
     * @param basicInformationMessage - базовые информация сообщения
     * @param sendMessage - сообщение для отправки пользователю
     * @param intermediateData - Сохраняет данных между запросами из бота
     */
    public void buttonProcessing(BasicInformationMessage basicInformationMessage,
                                 SendMessage sendMessage,
                                 Map<String, Intermediate> intermediateData)
    {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        String textMessage = basicInformationMessage.getMessageText();

        if(textMessage.equals(CommandConstant.CANCEL_CREATION_COMMANDS))
        {

            intermediateData.get(chatId).clearData();
            sendMessage.setText(TitleButtonConstant.CANCEL_CREATION_TITLE);
        }
        else if (textMessage.equals(ButtonConstant.ADD_FINANCIAL_ACCOUNT_COMMANDS)) {
            intermediateData.get(chatId.toString()).setFinancialAccount(new FinancialAccount());
            String answerText = StringConstant.INTRODUCTORY_TEXT_SELECTION_COMPANIES +
                    financialAccountFilling.getListFinancialOrganizationsAvailableNewAccount(sendMessage);
            sendMessage.setText(answerText);
        }
        else if(textMessage.contains(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS))
        {
            String firstPartText = textMessage.substring(1, textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS));
            String secondPartText = textMessage.substring(textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS) + 1);

            if(firstPartText.equals(StringConstant.ACCOUNT_TYPE))
            {
                Optional<AccountType> accountType = accountTypeRepository.findByTitle(secondPartText);
                intermediateData.get(chatId).getFinancialAccount().setAccountType(accountType.get());
                financialAccountFilling.addCurrencyButton(sendMessage);
                sendMessage.setText(StringConstant.ACCOUNT_TYPE_OK_SELECT_CURRENCY);

                return;
            }
            if(firstPartText.equals(StringConstant.CURRENCY))
            {
                Optional<Currency> currency = currencyRepository.findByTitle(secondPartText);
                if (!currency.isEmpty()) {
                    intermediateData.get(chatId).getFinancialAccount().setCurrency(currency.get());
                }

                financialAccountFilling.checkFillAllData(basicInformationMessage, sendMessage, intermediateData);

                return;
            }
            if(firstPartText.equals(ConstantAccountChange.SELECT_ACCOUNT))
            {
                Optional<UserTelegram> user = userRepository.findByChatId(basicInformationMessage.getUserTelegram().getChatId());
                String[] titleCompany = secondPartText.split(" ");
                Optional<FinancialAccount> financialAccount = userRepository.findFinancialAccountByUserAndOrganizationTitle(user.get().getId(), titleCompany[0]);
                intermediateData.get(chatId).getFinancialChange().setFinancialAccount(financialAccount.get());
                sendMessage.setText(ConstantAccountChange.EXPENSE_TYPE);
                fillingAccountChange.addButtonExpenseType(sendMessage);

                return;
            }
            if(firstPartText.equals(ConstantAccountChange.EXPENSE_TYPE))
            {
                Optional<ExpenseType> expensesType = expenseTypeRepository.findByTitle(secondPartText);
                intermediateData.get(chatId).getFinancialChange().setExpenseType(expensesType.get());

                fillingAccountChange.checkFillingFinancialChange(basicInformationMessage, sendMessage, intermediateData);
                return;
            }
        }
    }
}
