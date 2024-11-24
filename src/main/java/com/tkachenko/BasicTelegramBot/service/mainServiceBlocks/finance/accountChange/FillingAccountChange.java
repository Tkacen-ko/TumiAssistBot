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
import jakarta.transaction.Transactional;
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
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        FinancialChange financialChange = intermediateData.get(chatId).getFinancialChange();
        String textAnswer = "";

        if(financialChange == null) {
            financialChange =
                    fillingFinancialChangeObject.fillingFinancialChange(basicInformationMessage,
                            sendMessage,
                            intermediateData
                    );
        }
        else
        {
            textAnswer = StringConstant.NOT_FINISHED_FILLING_FINANCIAL_ORGANIZATION_SETTINGS;
        }

        if(sendMessage.getText() != null)
        {
            return;
        }

        if(financialChange.getFinancialAccount() == null)
        {
            textAnswer += ConstantAccountChange.SELECT_ACCOUNT;
            addButtonFinancialAccountUser(basicInformationMessage, sendMessage);
            sendMessage.setText(textAnswer);

            return;
        }

        if(financialChange.getExpenseType() == null)
        {
            textAnswer += ConstantAccountChange.SELECT_EXPENSE_TYPE;
            addButtonExpenseType(sendMessage);
            sendMessage.setText(textAnswer);

            return;
        }

        checkFillingFinancialChange(basicInformationMessage, sendMessage, intermediateData);
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
            FinancialAccount financialAccount = changeAndSaveAccountBalance(financialChange);
            financialChange.setCurrency(financialAccount.getCurrency());
            financialChangeRepository.save(financialChange);

            String answerText = getFinalTextMessageFinancialChange(intermediateData.get(chatId).getFinancialChange(), financialAccount);
            sendMessage.setText(answerText);
            intermediateData.get(chatId).clearData();
        }
    }

    String getFinancialOrganizationButtonTitle(String organizationTitle, String  organizationShortName)
    {
        return organizationTitle + " (" + organizationShortName +")";
    }

    String getFinalTextMessageFinancialChange(FinancialChange financialChange, FinancialAccount financialAccount)
    {
        return "Расход на сумму *" + financialChange.getAmount() +"* " + financialAccount.getCurrency().getTitle() +
                " сохранён!\n" + "Текущий баланс счёта " + financialAccount.getFinancialOrganization().getTitle()
                + ":\n*" + financialAccount.getBalance() +
                " " + financialAccount.getCurrency().getTitle()+ "*";
    }

    FinancialAccount changeAndSaveAccountBalance(FinancialChange financialChange)
    {
        Optional<FinancialAccount> financialAccount =
                financialAccountRepository.findByIdWithCurrencyAndOrganization(financialChange.getFinancialAccount().getId());
        if(!financialAccount.isEmpty())
        {
            financialAccount.get().setBalance(financialAccount.get().getBalance() + financialChange.getAmount());
            financialAccountRepository.save(financialAccount.get());
        }
        return financialAccount.get();
    }
}
