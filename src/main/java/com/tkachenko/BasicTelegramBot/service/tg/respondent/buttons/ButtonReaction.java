package com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.model.finance.organization.Country;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.general.CurrencyRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.CountryRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.intermediateData.IntermediateAnswerBuilder;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ButtonReaction {

    private final FinancialOrganizationRepository organizationRepository;
    private final IntermediateAnswerBuilder intermediateAnswerBuilder;
    private final AccountTypeRepository accountTypeRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;

    ButtonReaction(FinancialOrganizationRepository organizationRepository,
                   IntermediateAnswerBuilder accountBuilder,
                   AccountTypeRepository accountTypeRepository,
                   CountryRepository countryRepository,
                   CurrencyRepository currencyRepository)
    {
        this.organizationRepository = organizationRepository;
        this.intermediateAnswerBuilder = accountBuilder;
        this.accountTypeRepository = accountTypeRepository;
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
    }

    public SendMessage buttonProcessing(BasicInformationMessage basicInformationMessage,
                                        SendMessage sendMessage,
                                        Map<String, Intermediate> intermediateData))
    {
        //DOTO OLD CODE
//        Account account = intermediateData.get(basicInformationMessage.getIdChat());
//
//        boolean allFieldsNotNull = Stream.of(account.getTitle(),
//                account.getTotalMoney(), account.getType(),
//                account.getClassification(),account.getFinancialOrganization(),
//                account.getCountry(), account.getCurrency()).allMatch(Objects::nonNull);
//        if(allFieldsNotNull)
//        {
//            accountRepository.save(account);
//            intermediateData.clear();
//            String answerText = "Все данные счёта заполнены и счёт сохранён";
//            sendMessage.setText(answerText);
//        }


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
            InlineKeyboardMarkup inlineKeyboardMarkup = intermediateAnswerBuilder.fillingAccount(basicTelegramData, account, answererMessage);
            answererMessage.setReplyMarkup(inlineKeyboardMarkup);

            return answererMessage;
        }
        else if(command.contains("_"))
        {
            String firstPartText = command.substring(0, command.lastIndexOf("_"));
            String secondPartText = command.substring(command.lastIndexOf("_") + 1);
            if(firstPartText.equals("currency"))
            {
                Optional<Currency> organization = currencyRepository.getByTitle(secondPartText);
                if(!organization.isEmpty())
                {
                    account.setCurrency(organization.get());
                    InlineKeyboardMarkup inlineKeyboardMarkup = intermediateAnswerBuilder.fillingAccount(basicTelegramData, account, answererMessage);
                    answererMessage.setReplyMarkup(inlineKeyboardMarkup);

                    return answererMessage;
                }
            }
            if(firstPartText.equals("organization"))
            {
                Optional<FinancialOrganization> organization = organizationRepository.getByTitle(secondPartText);
                if(!organization.isEmpty())
                {
                    account.setFinancialOrganization(organization.get());
                    InlineKeyboardMarkup inlineKeyboardMarkup = intermediateAnswerBuilder.fillingAccount(basicTelegramData, account, answererMessage);
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
                    InlineKeyboardMarkup inlineKeyboardMarkup = intermediateAnswerBuilder.fillingAccount(basicTelegramData, account, answererMessage);
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
                    InlineKeyboardMarkup inlineKeyboardMarkup = intermediateAnswerBuilder.fillingAccount(basicTelegramData, account, answererMessage);
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
                    InlineKeyboardMarkup inlineKeyboardMarkup = intermediateAnswerBuilder.fillingAccount(basicTelegramData, account, answererMessage);
                    answererMessage.setReplyMarkup(inlineKeyboardMarkup);

                    return answererMessage;
                }
            }
        }

        answererMessage.setText(answerText);
        return answererMessage;
    }
}
