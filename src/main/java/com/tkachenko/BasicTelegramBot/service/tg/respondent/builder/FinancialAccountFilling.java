package com.tkachenko.BasicTelegramBot.service.tg.respondent.builder;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.financialAccount.FinancialAccountRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonReaction;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.FinancialCommandHandler;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;
import java.util.stream.Stream;

@Component
public class FinancialAccountFilling {

    private final FinancialOrganizationRepository financialOrganizationRepository;
    private final FinancialCommandHandler financialCommandHandler;
    private final ButtonReaction buttonReaction;
    private final AccountTypeRepository accountTypeRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final FinancialAccountRepository financialAccountRepository;

    @Autowired
    FinancialAccountFilling(FinancialOrganizationRepository financialOrganizationRepository,
                            FinancialCommandHandler financialCommandHandler,
                            ButtonReaction buttonReaction,
                            AccountTypeRepository accountTypeRepository,
                            ButtonsBuilder buttonsBuilder,
                            FinancialAccountRepository financialAccountRepository)
    {
        this.financialOrganizationRepository = financialOrganizationRepository;
        this.financialCommandHandler = financialCommandHandler;
        this.buttonReaction = buttonReaction;
        this.accountTypeRepository = accountTypeRepository;
        this.buttonsBuilder = buttonsBuilder;
        this.financialAccountRepository = financialAccountRepository;
    }

    @Transactional
    public void checkFillingDataFinancialAccount(BasicInformationMessage basicInformationMessage,
                                                 SendMessage sendMessage,
                                                 Map<String, Intermediate> intermediateData,
                                                 FinancialAccount financialAccount) {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        String textMessage = basicInformationMessage.getMessageText();
        InlineKeyboardMarkup keyboardMarkup = null;
        Map<String, String> buttonData = new LinkedHashMap<>();
        String firstPartText = null;
        String secondPartText = null;

        if (textMessage.contains(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS)) {
            firstPartText = textMessage.substring(1, textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS));
            secondPartText = textMessage.substring(textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS) + 1);
        }

        if (financialAccount.getFinancialOrganizations() == null)
        {
            financialCommandHandler.addFinancialOrganizationsUser(sendMessage, chatId, basicInformationMessage, intermediateData);
            if(sendMessage.getText() != null)
            {
                return;
            }
        }

        if (financialAccount.getTitle() == null && firstPartText.equals(StringConstant.ORGANIZATION))
        {
            Optional<FinancialOrganization> organization = financialOrganizationRepository.findByTitle(secondPartText);
            if (!organization.isEmpty()) {
                String titleFinancialAccount = organization.get().getTitle() +
                        "(" +
                        organization.get().getCountry().getTitle() +
                        "), " +
                        organization.get().getTypeOrganization().getTitle() +
                        ".";
                financialAccount.setTitle(titleFinancialAccount);
            }
        }

        if (financialAccount.getAccountType() == null && firstPartText != null)
        {
            if(firstPartText.equals(StringConstant.ORGANIZATION))
            {
                sendMessage.setText(StringConstant.SELECT_ACCOUNT_TYPE);
                for (String accountType : accountTypeRepository.findAllTitles()) {
                    String commandButton = StringConstant.BASIC_COMMAND_SEPARATOR+
                            StringConstant.ACCOUNT_TYPE +
                            ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS +
                            accountType;
                    buttonData.put(accountType, commandButton);
                }
                keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
                sendMessage.setReplyMarkup(keyboardMarkup);

                return;
            }
            else
            {
                buttonReaction.buttonProcessing(basicInformationMessage, sendMessage, intermediateData);
            }

            return;
        }

        if (financialAccount.getCurrency() == null)
        {
            buttonReaction.buttonProcessing(basicInformationMessage, sendMessage, intermediateData);
        }

        FinancialAccount account = intermediateData.get(chatId.toString()).getFinancialAccount();

        boolean allFieldsNotNull = Stream.of(
                account.getTitle(),
                financialAccount.getAccountType(), account.getCurrency()
        ).allMatch(Objects::nonNull);

        if (allFieldsNotNull) {
            financialAccountRepository.save(account);
            intermediateData.get(chatId.toString()).clearData();
            String answerText = "Все данные счёта заполнены и счёт сохранён";
            sendMessage.setText(answerText);
        }
    }
}
