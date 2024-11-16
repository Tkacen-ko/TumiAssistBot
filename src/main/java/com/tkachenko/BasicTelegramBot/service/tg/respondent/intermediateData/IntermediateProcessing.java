package com.tkachenko.BasicTelegramBot.service.tg.respondent.intermediateData;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.builder.FinancialAccountFilling;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Service
public class IntermediateProcessing {

    private final ButtonsBuilder buttonsBuilder;
    private final FinancialAccountFilling financialAccountFilling;

    @Autowired
    IntermediateProcessing(ButtonsBuilder buttonsBuilder,
                           FinancialAccountFilling financialAccountFilling)
    {
        this.buttonsBuilder = buttonsBuilder;
        this.financialAccountFilling = financialAccountFilling;
    }

    /**
     * Метод ответа когда есть промежуточные данные от пользователя
     * @param basicInformationMessage - базовые информация сообщения
     * @param sendMessage - сообщение для отправки пользователю
     * @param intermediateData - промежуточные между ТГ запросами, сообщения пользователя данные
     */
    public void checkingForIntermediateData(BasicInformationMessage basicInformationMessage,
                                            SendMessage sendMessage,
                                            Map<String, Intermediate> intermediateData)
    {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();

        if(intermediateData.get(chatId) == null)
        {
            return;
        }

        Intermediate intermediate = intermediateData.get(chatId);
        FinancialAccount financialAccount =  intermediate.getFinancialAccount();

        if(financialAccount != null)
        {
            financialAccountFilling.checkFillingDataFinancialAccount(   basicInformationMessage,
                                                                        sendMessage,
                                                                        financialAccount);

            return;
        }
    }
}
