package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.dto.BasicTelegramData;
import com.tkachenko.BasicTelegramBot.model.finance.Account;
import com.tkachenko.BasicTelegramBot.repository.finance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AccountBuilder {

    private final ButtonsBuilder buttonsBuilder;
    private final OrganizationRepository organizationRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final ClassificationRepository classificationRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public AccountBuilder(ButtonsBuilder buttonsBuilder,
                          OrganizationRepository organizationRepository,
                          AccountTypeRepository accountTypeRepository,
                          ClassificationRepository classificationRepository,
                          CountryRepository countryRepository,
                          CurrencyRepository currencyRepository)
    {
        this.buttonsBuilder = buttonsBuilder;
        this.organizationRepository = organizationRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.classificationRepository = classificationRepository;
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
    }

    InlineKeyboardMarkup fillingAccount(BasicTelegramData basicTelegramData,
                                        Account account,
                                        SendMessage answererMessage)    {
        Map<String, String> buttonData = new LinkedHashMap<>();

        if(account.getTitle() == null)
        {
            answererMessage.setText("Определите и напишите в чат подпись для актива. Либо нажнимите кпопку отмены.");
        }
        else if(account.getTotalMoney() == null)
        {
            answererMessage.setText("Напишите сумму которая храниться в рамках данного аккаунта");
        }
        else if(account.getCurrency() == null)
        {
            for (String organization : currencyRepository.findAllTitles())
            {
                buttonData.put(organization, ("@currency_" + organization));
            }

            answererMessage.setText("Выберете вылюту, либо нажнимите кпопку отмены.");
        }
        else if(account.getOrganization() == null)
        {
            for (String organization : organizationRepository.findAllTitles())
            {
                buttonData.put(organization, ("@organization_" + organization));
            }

            answererMessage.setText("Выберете организацию, либо нажнимите кпопку отмены.");
        }
        else if(account.getType() == null)
        {
            for (String organization : accountTypeRepository.findAllTitles())
            {
                buttonData.put(organization, ("@accountType_" + organization));
            }

            answererMessage.setText("Выберете тип (наличные, банковский счёт и т.д.), либо нажнимите кпопку отмены.");
        }
        else if(account.getClassification() == null)
        {
            for (String classification : classificationRepository.findAllTitles())
            {
                buttonData.put(classification, ("@classification_" + classification));
            }

            answererMessage.setText("Выберете классификацию (текущие рассходы, подушка безопасности т.д.), либо нажнимите кпопку отмены.");
        }
        else if(account.getCountry() == null)
        {
            for (String country : countryRepository.findAllTitles())
            {
                buttonData.put(country, ("@country_" + country));
            }

            answererMessage.setText("Выберете резеденцию актива (РФ, США, Кипр т.д.), либо нажнимите кпопку отмены.");
        }

        buttonData.put("Отмена создания", "@cancel_creation");
        InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);

        answererMessage.setReplyMarkup(keyboardMarkup);
        return keyboardMarkup;
    }
}
