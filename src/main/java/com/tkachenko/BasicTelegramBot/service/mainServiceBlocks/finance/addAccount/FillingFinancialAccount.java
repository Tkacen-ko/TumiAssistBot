package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.addAccount;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.financialAccount.FinancialAccountRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.general.CurrencyRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class FillingFinancialAccount {
    private final ButtonsBuilder buttonsBuilder;
    private final UserRepository userRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final FinancialOrganizationRepository financialOrganizationRepository;
    private final CurrencyRepository currencyRepository;
    private final FinancialAccountRepository financialAccountRepository;

    @Autowired
    FillingFinancialAccount(ButtonsBuilder buttonsBuilder,
                            UserRepository userRepository,
                            AccountTypeRepository accountTypeRepository,
                            FinancialOrganizationRepository financialOrganizationRepository,
                            CurrencyRepository currencyRepository,
                            FinancialAccountRepository financialAccountRepository)
    {
        this.buttonsBuilder = buttonsBuilder;
        this.userRepository = userRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.financialOrganizationRepository = financialOrganizationRepository;
        this.currencyRepository = currencyRepository;
        this.financialAccountRepository = financialAccountRepository;
    }

    @Transactional
    public void checkFillingDataFinancialAccount(BasicInformationMessage basicInformationMessage,
                                                 SendMessage sendMessage,
                                                 Map<String, Intermediate> intermediateData) {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        FinancialAccount financialAccount = intermediateData.get(chatId).getFinancialAccount();
        String answerText = "Не выбран не один из пунктов базового заполнения";

        if(financialAccount.getFinancialOrganizations() == null)
        {
            saveNewUserAccount(sendMessage, basicInformationMessage, intermediateData);
            return;
        }
        if(financialAccount.getAccountType() == null)
        {
            addAccountTypeButton(sendMessage);
            sendMessage.setText(StringConstant.NOT_FINISHED_FILLING_FINANCIAL_ORGANIZATION_SETTINGS + StringConstant.SELECT_ACCOUNT_TYPE);
            return;
        }
        if(financialAccount.getCurrency() == null)
        {
            addCurrencyButton(sendMessage);
            sendMessage.setText(StringConstant.NOT_FINISHED_FILLING_FINANCIAL_ORGANIZATION_SETTINGS + StringConstant.SELECT_ACCOUNT_CURRENCY);
        }
    }

    @Transactional
    public String getListFinancialOrganizationsAvailableNewAccount(SendMessage sendMessage)
    {
        InlineKeyboardMarkup keyboardMarkup = null;
        Map<String, String> buttonData = new LinkedHashMap<>();

        List<FinancialOrganization> financialOrganizations = financialOrganizationRepository.findAll();
        String textNewMessage = IntStream.range(0, financialOrganizations.size())
                .mapToObj(i -> String.format(
                        "*%d*. %s (%s), %s.",
                        i + 1, // Нумерация начинается с 1
                        financialOrganizations.get(i).getTitle(),
                        financialOrganizations.get(i).getCountry().getTitle(),
                        financialOrganizations.get(i).getTypeOrganization().getTitle()
                ))
                .collect(Collectors.joining("\n", "", ""));
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        keyboardMarkup = buttonsBuilder.createMessageWithButtons(ButtonConstant.CANCEL_CREATION);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return textNewMessage;
    }

    @Transactional
    public void saveNewUserAccount(SendMessage sendMessage,
                                   BasicInformationMessage basicInformationMessage,
                                   Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        String textMessage = basicInformationMessage.getMessageText();

        int numberIdCompany = parseNumbers(textMessage, sendMessage);
        if(sendMessage.getText() != null || numberIdCompany == -1)
        {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION + getListFinancialOrganizationsAvailableNewAccount(sendMessage));
            return;
        }
        Optional<FinancialOrganization> financialOrganization = financialOrganizationRepository.findById(numberIdCompany);
        if(financialOrganization.isEmpty())
        {
            sendMessage.setText(StringConstant.INVALID_COMPANY_NUMBER + getListFinancialOrganizationsAvailableNewAccount(sendMessage));
            return;
        }
        UserTelegram currentUser = userRepository.findByChatId(chatId).get();
        List<FinancialOrganization> financialOrganizationList = new ArrayList<>();
        financialOrganizationList.add(financialOrganization.get());

        FinancialAccount financialAccount = intermediateData.get(basicInformationMessage.getUserTelegram().getChatId().toString())
                .getFinancialAccount();
        financialAccount.setFinancialOrganizations(financialOrganizationList);
        financialAccount.setFinancialOrganization(financialOrganization.get());
        userRepository.save(currentUser);
        sendMessage.setText(StringConstant.LIST_FINANCIAL_COMPANIES_ESTABLISHED + StringConstant.SELECT_ACCOUNT_TYPE);
        addAccountTypeButton(sendMessage);
    }

    public void addCurrencyButton(SendMessage sendMessage)
    {

        Map<String, String> buttonData = new LinkedHashMap<>();
        for (String currency : currencyRepository.findAllTitles())
        {
            String commandCode = StringConstant.BASIC_COMMAND_SEPARATOR +
                    String.join(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS,
                            new String[]{ StringConstant.CURRENCY, currency });
            buttonData.put(currency, commandCode);
        }
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);

        InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public void addAccountTypeButton(SendMessage sendMessage)
    {
        Map<String, String> buttonData = new LinkedHashMap<>();
        for (String accountType : accountTypeRepository.findAllTitles()) {
            String commandButton = StringConstant.BASIC_COMMAND_SEPARATOR+
                    StringConstant.ACCOUNT_TYPE +
                    ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS +
                    accountType;
            buttonData.put(accountType, commandButton);
        }
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private int parseNumbers(String input, SendMessage sendMessage) {
        int number = -1;

        if (input == null || input.isBlank()) {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION + getListFinancialOrganizationsAvailableNewAccount(sendMessage));
        }

        try {
            number = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION + getListFinancialOrganizationsAvailableNewAccount(sendMessage));
        }

        return number;
    }

    @Transactional
    public void checkFillAllData(BasicInformationMessage basicInformationMessage,
                                 SendMessage sendMessage,
                                 Map<String, Intermediate> intermediateData)
    {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        FinancialAccount financialAccount = intermediateData.get(chatId).getFinancialAccount();
        boolean allFieldsNotNull = Stream.of(
                financialAccount.getFinancialOrganization(),
                financialAccount.getAccountType(), financialAccount.getCurrency()
        ).allMatch(Objects::nonNull);

        if (allFieldsNotNull) {
            Optional<UserTelegram> user = userRepository.findByChatId(basicInformationMessage.getUserTelegram().getChatId());
            financialAccountRepository.save(financialAccount);
            user.get().getFinancialAccounts().add(financialAccount);
            userRepository.save(user.get());
            String answerText = getFinalStringDataCreatedAccount(intermediateData.get(chatId).getFinancialAccount());
            sendMessage.setText(answerText);
            intermediateData.get(chatId).clearData();
        }
    }

    private String getFinalStringDataCreatedAccount(FinancialAccount financialAccount)
    {
        return "Отлично!\nВы добавили новый счёт в: *" + financialAccount.getFinancialOrganization().getTitle() + "*" +
                "\nВалюта счёта: *" + financialAccount.getCurrency().getTitle() + "*." +
                "\nТекущий баланс: *" + financialAccount.getBalance() + "*." +
                "\n\nЧтобы пополнить счёт или снять средства, просто отправьте мне сообщение в формате: " +
                "`+100` или `-200`. Я сохраню эту операцию, если всё будет указано корректно." +
                "\nСпасибо за вашу активность!";
    }
}
