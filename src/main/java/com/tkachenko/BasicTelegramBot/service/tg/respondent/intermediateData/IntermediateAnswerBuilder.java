package com.tkachenko.BasicTelegramBot.service.tg.respondent.intermediateData;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.repository.finance.general.CurrencyRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.CountryRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class IntermediateAnswerBuilder {

    private final ButtonsBuilder buttonsBuilder;
    private final FinancialOrganizationRepository organizationRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public IntermediateAnswerBuilder(ButtonsBuilder buttonsBuilder,
                                     FinancialOrganizationRepository organizationRepository,
                                     AccountTypeRepository accountTypeRepository,
                                     CountryRepository countryRepository,
                                     CurrencyRepository currencyRepository)
    {
        this.buttonsBuilder = buttonsBuilder;
        this.organizationRepository = organizationRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
    }



    InlineKeyboardMarkup fillingAccount(BasicInformationMessage basicTelegramData,
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
        else if(account.getFinancialOrganization() == null)
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
