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
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.psychologicalHealth.FillUserEmotion;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.psychologicalHealth.FillingThoughtUser;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.TitleButtonConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ButtonReaction {

    private final CurrencyRepository currencyRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final FillingFinancialAccount financialAccountFilling;
    private final UserRepository userRepository;
    private final FillingAccountChange fillingAccountChange;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final FillingThoughtUser fillingThoughtUser;
    private final FillUserEmotion fillUserEmotion;

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
        String answerText = "";

        switch (textMessage)
        {
            case ButtonConstant.CANCEL_CREATION_BUTTON_CODE:
                intermediateData.get(chatId).clearData();
                sendMessage.setText(TitleButtonConstant.CANCEL_TITLE);

                return;

            case ButtonConstant.CREATE_ACCOUNT_BUTTON_CODE:
                intermediateData.get(chatId.toString()).setFinancialAccount(new FinancialAccount());
                answerText = StringConstant.INTRODUCTORY_TEXT_SELECTION_COMPANIES +
                        financialAccountFilling.getListFinancialOrganizationsAvailableNewAccount(sendMessage);
                sendMessage.setText(answerText);

                return;

            case ButtonConstant.GET_INFO_FINANCE_BUTTON_CODE:
                sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(ButtonConstant.FINANCE_BLOCK));
                sendMessage.setText(StringConstant.FINANCIAL_BLOCK_FUNCTIONALITY_MESSAGE);

                return;

            case ButtonConstant.GET_INFO_HEALTH_BUTTON_CODE:
                sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(ButtonConstant.PHYSICAL_HEALTH));
                sendMessage.setText(StringConstant.PHYSICAL_HEALTH_BLOCK_FUNCTIONALITY_MESSAGE);

                return;

            case ButtonConstant.GET_INFO_MENTAL_BUTTON_CODE:
                sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(ButtonConstant.MENTAL_HEALTH));
                sendMessage.setText(StringConstant.MENTAL_HEALTH_BLOCK_FUNCTIONALITY_MESSAGE);

                return;

            case ButtonConstant.CHANGE_ACCOUNT_BUTTON_CODE:
                sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(ButtonConstant.CHANGE_ACCOUNT));
                sendMessage.setText(StringConstant.CREATE_DELETE_EDIT_ACCOUNT_MESSAGE);

                return;

            case ButtonConstant.ENTERING_INCOME_EXPENSE_BUTTON_CODE:
                sendMessage.setText(StringConstant.CHANGE_OF_ACCOUNT_BALANCE_MESSAGE);

                return;

            case ButtonConstant.ANALYSIS_FINANCE_BUTTON_CODE:
                sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(ButtonConstant.OPTIONS_FOR_ANALYZING_EXPENSES_AND_INCOME));
                sendMessage.setText(StringConstant.ANALYSIS_EXPENSES_AND_INCOME_MESSAGE);

                return;

            case ButtonConstant.SAVE_AS_THOUGHT_BUTTON_CODE:
                fillingThoughtUser.fillThought(basicInformationMessage, sendMessage, intermediateData);

                return;

            case ButtonConstant.SAVE_THOUGHT_DEFAULT_BUTTON_CODE:
                fillingThoughtUser.saveThought(basicInformationMessage, sendMessage, intermediateData);
                fillUserEmotion.checkAndCreateEmotion(basicInformationMessage, sendMessage, intermediateData);
                intermediateData.get(chatId.toString()).clearData();
                String textAnswer = "Ваша мысль успешно сохранена. \nПри необходимости вы всегда можете её найти по тегу, " +
                        "просто написав чат сообщение в формате \"#тегМысль\"";
                sendMessage.setText(textAnswer);

                return;

            case ButtonConstant.ADD_TAGS_AND_EMOTIONS_BUTTON_CODE:
                fillingThoughtUser.addTagsAndEmotions(basicInformationMessage, sendMessage, intermediateData);

                return;

            case ButtonConstant.ADD_HEALTH_DATA_BUTTON_CODE:
            case ButtonConstant.AUTOMATE_DATA_COLLECTION_BUTTON_CODE:
            case ButtonConstant.THOUGHT_RECORDINGS_BUTTON_CODE:
            case ButtonConstant.EMOTIONAL_DIARY_BUTTON_CODE:
            case ButtonConstant.ANALYSIS_AND_SUPPORT_BUTTON_CODE:
            case ButtonConstant.DELETE_ACCOUNT_BUTTON_CODE:
            case ButtonConstant.EDIT_ACCOUNT_BUTTON_CODE:
            case ButtonConstant.WEEK_ANALYSIS_FINANCE_BUTTON_CODE:
            case ButtonConstant.MONTH_ANALYSIS_FINANCE_BUTTON_CODE:
            case ButtonConstant.YEAR_ANALYSIS_FINANCE_BUTTON_CODE:
            case ButtonConstant.AI_ANALYSIS_FINANCE_BUTTON_CODE:
                sendMessage.setText(StringConstant.FUNCTIONALITY_UNDER_DEVELOPMENT);

                return;
        }

        if(!textMessage.contains(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS))
        {
            return;
        }

        String firstPartText = textMessage.substring(1, textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS));
        String secondPartText = textMessage.substring(textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS) + 1);
        Optional<AccountType> accountType = null;

        switch (firstPartText)
        {
            case StringConstant.ACCOUNT_TYPE:
                accountType = accountTypeRepository.findByTitle(secondPartText);
                intermediateData.get(chatId).getFinancialAccount().setAccountType(accountType.get());
                financialAccountFilling.addCurrencyButton(sendMessage);
                sendMessage.setText(StringConstant.ACCOUNT_TYPE_OK_SELECT_CURRENCY);

                return;

            case StringConstant.CURRENCY:
                Optional<Currency> currency = currencyRepository.findByTitle(secondPartText);
                if (!currency.isEmpty()) {
                    intermediateData.get(chatId).getFinancialAccount().setCurrency(currency.get());
                }
                financialAccountFilling.checkFillAllData(basicInformationMessage, sendMessage, intermediateData);

                return;

            case ConstantAccountChange.FINANCIAL_ACCOUNT:
                Optional<UserTelegram> user = userRepository.findByChatId(basicInformationMessage.getUserTelegram().getChatId());
                Optional<FinancialAccount> financialAccount = userRepository.findFinancialAccountByUserAndOrganizationTitle(user.get().getId(), secondPartText);
                intermediateData.get(chatId).getFinancialChange().setFinancialAccount(financialAccount.get());
                sendMessage.setText(ConstantAccountChange.SELECT_EXPENSE_TYPE);
                fillingAccountChange.addButtonExpenseType(sendMessage);

                return;

            case ConstantAccountChange.EXPENSE_TYPE:
                Optional<ExpenseType> expensesType = expenseTypeRepository.findByTitle(secondPartText);
                intermediateData.get(chatId).getFinancialChange().setExpenseType(expensesType.get());
                fillingAccountChange.checkFillingFinancialChange(basicInformationMessage, sendMessage, intermediateData);

                return;
        }
    }
}
