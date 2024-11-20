package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.accountChange;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.expenses.ExpenseType;
import com.tkachenko.BasicTelegramBot.model.finance.expenses.FinancialChange;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.expenses.ExpenseTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.expenses.FinancialChangeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.financialAccount.FinancialAccountRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * Метод заполнения данных о изменениях на счетах пользователя
 */
@Service
public class FillingAccountChange {
    private final FillingFinancialChangeObject fillingFinancialChangeObject;
    private final UserRepository userRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final FinancialChangeRepository financialChangeRepository;
    private final FinancialAccountRepository financialAccountRepository;

    @Autowired
    FillingAccountChange(FillingFinancialChangeObject fillingFinancialChangeObject,
                         UserRepository userRepository,
                         ExpenseTypeRepository expenseTypeRepository,
                         ButtonsBuilder buttonsBuilder,
                         FinancialChangeRepository financialChangeRepository,
                         FinancialAccountRepository financialAccountRepository)
    {
        this.fillingFinancialChangeObject = fillingFinancialChangeObject;
        this.userRepository = userRepository;
        this.expenseTypeRepository = expenseTypeRepository;
        this.buttonsBuilder = buttonsBuilder;
        this.financialChangeRepository = financialChangeRepository;
        this.financialAccountRepository = financialAccountRepository;
    }

    /**
     * Метод пополнения счёта пользователя
     * @param basicInformationMessage - базовые информация сообщения
     * @param sendMessage - сообщение для отправки пользователю
     * @param intermediateData - Сохраняет данных между запросами из бота
     */
    public void balanceChange(BasicInformationMessage basicInformationMessage,
                              SendMessage sendMessage,
                              Map<String, Intermediate> intermediateData)
    {
        FinancialChange financialChange =
                fillingFinancialChangeObject.fillingFinancialChange(basicInformationMessage,
                        sendMessage,
                        intermediateData
                );

        if(sendMessage.getText() != null)
        {
            return;
        }

        if(financialChange.getFinancialAccount() == null)
        {
            sendMessage.setText(ConstantAccountChange.SELECT_ACCOUNT);
            addButtonFinancialAccountUser(basicInformationMessage, sendMessage);

            return;
        }

        if(financialChange.getExpenseType() == null)
        {
            sendMessage.setText(ConstantAccountChange.EXPENSE_TYPE);
            addButtonExpenseType(sendMessage);
        }
    }

    void addButtonFinancialAccountUser(BasicInformationMessage basicInformationMessage,
                                       SendMessage sendMessage)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        Optional<UserTelegram> user = userRepository.findByChatIdWithAccountsAndOrganizations(chatId);

        if(!user.isEmpty())
        {
            List<FinancialAccount> financeAccount =  user.get().getFinancialAccounts();
            Map<String, String> buttonData = new LinkedHashMap<>();
            for (FinancialAccount financialAccount : financeAccount)
            {
                String titleFinancialAccount = financialAccount.getFinancialOrganization().getTitle();
                String commandCode = StringConstant.BASIC_COMMAND_SEPARATOR +
                        String.join(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS,
                                new String[]{ ConstantAccountChange.FINANCIAL_ACCOUNT, titleFinancialAccount });
                buttonData.put(titleFinancialAccount, commandCode);
            }
            buttonData.putAll(ButtonConstant.CANCEL_CREATION);

            InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
            sendMessage.setReplyMarkup(keyboardMarkup);
        }
    }

    public void addButtonExpenseType(SendMessage sendMessage)
    {
        List<ExpenseType> expensesType = expenseTypeRepository.findAll();
            Map<String, String> buttonData = new LinkedHashMap<>();
            for (ExpenseType expenseType : expensesType)
            {
                String commandCode = StringConstant.BASIC_COMMAND_SEPARATOR +
                        String.join(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS,
                                new String[]{ ConstantAccountChange.EXPENSE_TYPE, expenseType.getTitle() });
                buttonData.put(expenseType.getTitle(), commandCode);
            }
            buttonData.putAll(ButtonConstant.CANCEL_CREATION);

            InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
            sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public void checkFillingFinancialChange(BasicInformationMessage basicInformationMessage,
                                            SendMessage sendMessage,
                                            Map<String, Intermediate> intermediateData)
    {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        FinancialChange financialChange = intermediateData.get(chatId).getFinancialChange();
        boolean allFieldsNotNull = Stream.of(
                financialChange.getAmount(),
                financialChange.getFinancialAccount(), financialChange.getExpenseType()
        ).allMatch(Objects::nonNull);

        if (allFieldsNotNull) {
            financialChange.setCreatedAt(LocalDateTime.now());
            financialChange.setAmount(financialChange.getAmount() + financialChange.getFinancialAccount().getBalance());
            financialChangeRepository.save(financialChange);
            financialAccountRepository.save(financialChange.getFinancialAccount());
            String answerText = getFinalTextMessageFinancialChange(intermediateData.get(chatId).getFinancialChange());
            sendMessage.setText(answerText);
            intermediateData.get(chatId).clearData();
        }
    }

    String getFinancialOrganizationButtonTitle(String organizationTitle, String  organizationShortName)
    {
        return organizationTitle + " (" + organizationShortName +")";
    }

    String getFinalTextMessageFinancialChange(FinancialChange financialChange)
    {
        return "Расход на сумму " + financialChange.getAmount() +" " + financialChange.getFinancialAccount().getCurrency().getTitle() +
                "сохранён!\n" + "Текущий баланс счёта " + financialChange.getFinancialAccount().getFinancialOrganization().getTitle()
                + "" + financialChange.getFinancialAccount().getBalance() +
                " " + financialChange.getFinancialAccount().getCurrency().getTitle();
    }
}
