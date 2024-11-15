package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.dto.BasicTelegramData;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.*;
import com.tkachenko.BasicTelegramBot.repository.finance.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ButtonReaction {

    private final OrganizationRepository organizationRepository;
    private final AccountBuilder accountBuilder;
    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final ClassificationRepository classificationRepository;
    private final CountryRepository countryRepository;

    ButtonReaction(OrganizationRepository organizationRepository,
                   AccountBuilder accountBuilder,
                   AccountRepository accountRepository,
                   AccountTypeRepository accountTypeRepository,
                   ClassificationRepository classificationRepository,
                   CountryRepository countryRepository)
    {
        this.organizationRepository = organizationRepository;
        this.accountBuilder = accountBuilder;
        this.accountRepository = accountRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.classificationRepository = classificationRepository;
        this.countryRepository = countryRepository;
    }

    SendMessage buttonProcessing(BasicTelegramData basicTelegramData,
                                 String command,
                                 Map<String, Account> temporaryAccounts,
                                 SendMessage answererMessage)
    {
        String answerText = "Неизвестная кнопка";
        Account account = temporaryAccounts.get(basicTelegramData.getIdChat());

        if (command.equals("continue_creating")) {
            answerText = "Типа продолжим тебя задолбывать";
        }
        else if(command.equals("cancel_creation"))
        {
            temporaryAccounts.clear();
            answerText = "Ну всё, затёр, не буду запаривать";
        }
        else if(command.equals("cancel_creation"))
        {
            temporaryAccounts.clear();
            answerText = "Ну всё, затёр, не буду запаривать";
        }
        else if(command.equals("continue_initializing_account"))
        {
            InlineKeyboardMarkup inlineKeyboardMarkup = accountBuilder.fillingAccount(basicTelegramData, account, answererMessage);
            answererMessage.setReplyMarkup(inlineKeyboardMarkup);

            return answererMessage;
        }
        else if(command.contains("_"))
        {
            String firstPartText = command.substring(0, command.lastIndexOf("_"));
            String secondPartText = command.substring(command.lastIndexOf("_") + 1);
            if(firstPartText.equals("organization"))
            {
                Optional<Organization> organization = organizationRepository.getByTitle(secondPartText);
                if(!organization.isEmpty())
                {
                    account.setOrganization(organization.get());
                    InlineKeyboardMarkup inlineKeyboardMarkup = accountBuilder.fillingAccount(basicTelegramData, account, answererMessage);
                    answererMessage.setReplyMarkup(inlineKeyboardMarkup);

                    return answererMessage;
                }
            }
            else if(firstPartText.equals("accountType"))
            {
                Optional<AccountType> accountType = accountTypeRepository.getByTitle(secondPartText);
                if(!accountType.isEmpty())
                {
                    account.setType(accountType.get());
                    InlineKeyboardMarkup inlineKeyboardMarkup = accountBuilder.fillingAccount(basicTelegramData, account, answererMessage);
                    answererMessage.setReplyMarkup(inlineKeyboardMarkup);

                    return answererMessage;
                }
            }
            else if(firstPartText.equals("classification"))
            {
                Optional<Classification> classification = classificationRepository.getByTitle(secondPartText);
                if(!classification.isEmpty())
                {
                    account.setClassification(classification.get());
                    InlineKeyboardMarkup inlineKeyboardMarkup = accountBuilder.fillingAccount(basicTelegramData, account, answererMessage);
                    answererMessage.setReplyMarkup(inlineKeyboardMarkup);

                    return answererMessage;
                }
            }
            else if(firstPartText.equals("country"))
            {
                Optional<Country> country = countryRepository.getByTitle(secondPartText);
                if(!country.isEmpty())
                {
                    account.setCountry(country.get());
                    InlineKeyboardMarkup inlineKeyboardMarkup = accountBuilder.fillingAccount(basicTelegramData, account, answererMessage);
                    answererMessage.setReplyMarkup(inlineKeyboardMarkup);

                    return answererMessage;
                }
            }
        }

        answererMessage.setText(answerText);
        return answererMessage;
    }
}
