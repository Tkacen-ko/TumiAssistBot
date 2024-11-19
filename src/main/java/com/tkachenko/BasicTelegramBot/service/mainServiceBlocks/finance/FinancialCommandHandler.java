package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.CommandConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FinancialCommandHandler {
    private final FinancialOrganizationRepository financialOrganizationRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final UserRepository userRepository;
    private final AccountTypeRepository accountTypeRepository;

    @Autowired
    FinancialCommandHandler(FinancialOrganizationRepository financialOrganizationRepository,
                            ButtonsBuilder buttonsBuilder,
                            UserRepository userRepository,
                            AccountTypeRepository accountTypeRepository)
    {
        this.financialOrganizationRepository = financialOrganizationRepository;
        this.buttonsBuilder = buttonsBuilder;
        this.userRepository = userRepository;
        this.accountTypeRepository = accountTypeRepository;
    }

    @Transactional
    public void getListFinancialOrganizationsAvailableNewAccount(SendMessage sendMessage)
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


        if(sendMessage.getText() == null)
        {
            String allTextMessage = StringConstant.INTRODUCTORY_TEXT_SELECTION_COMPANIES + textNewMessage;
            sendMessage.setText(allTextMessage);
        }

        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        keyboardMarkup = buttonsBuilder.createMessageWithButtons(ButtonConstant.CANCEL_CREATION);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    @Transactional
    public void saveNewUserAccount(SendMessage sendMessage,
                                   BasicInformationMessage basicInformationMessage,
                                   Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        intermediateData.get(chatId.toString()).setFinancialAccount(new FinancialAccount());
        String textMessage = basicInformationMessage.getMessageText();
        Map<String, String> buttonData = new LinkedHashMap<>();

        int numberIdCompany = parseNumbers(textMessage, sendMessage);
        if(sendMessage.getText() != null || numberIdCompany == -1)
        {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
            return;
        }
        Optional<FinancialOrganization> financialOrganization = financialOrganizationRepository.findById(numberIdCompany);
        if(financialOrganization.isEmpty())
        {
            sendMessage.setText(StringConstant.INVALID_COMPANY_NUMBER);
            return;
        }
        UserTelegram currentUser = userRepository.findByChatId(chatId).get();
        List<FinancialOrganization> financialOrganizationList = new ArrayList<>();
        financialOrganizationList.add(financialOrganization.get());

        FinancialAccount financialAccount = intermediateData.get(basicInformationMessage.getUserTelegram().getChatId().toString())
                .getFinancialAccount();
        financialAccount.setFinancialOrganizations(financialOrganizationList);
        String titleFinancialAccount = financialOrganization.get().getTitle() +
                "(" +
                financialOrganization.get().getCountry().getTitle() +
                "), " +
                financialOrganization.get().getTypeOrganization().getTitle() +
                ".";
        financialAccount.setTitle(titleFinancialAccount);
        userRepository.save(currentUser);
        sendMessage.setText(StringConstant.LIST_FINANCIAL_COMPANIES_ESTABLISHED + StringConstant.SELECT_ACCOUNT_TYPE);
        for (String accountType : accountTypeRepository.findAllTitles()) {
            String commandButton = StringConstant.BASIC_COMMAND_SEPARATOR+
                    StringConstant.ACCOUNT_TYPE +
                    ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS +
                    accountType;
            buttonData.put(accountType, commandButton);
        }
        InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private int parseNumbers(String input, SendMessage sendMessage) {
        int number = -1;

        if (input == null || input.isBlank()) {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
        }

        try {
            number = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
        }

        return number;
    }
}
