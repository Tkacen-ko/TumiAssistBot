package com.tkachenko.BasicTelegramBot.service.tg.respondent.intermediateData;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.accountChange.FillingAccountChange;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.addAccount.FillingFinancialAccount;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.psychologicalHealth.FillingThoughtUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Service
@AllArgsConstructor
public class IntermediateProcessing {
    private final FillingFinancialAccount financialAccountFilling;
    private final FillingAccountChange fillingAccountChange;
    private final FillingThoughtUser fillingThoughtUser;

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

        Intermediate intermediate = intermediateData.get(chatId);

        if(intermediate.getFinancialAccount() != null) {
            financialAccountFilling.checkFillingDataFinancialAccount(basicInformationMessage,
                    sendMessage,
                    intermediateData
            );

            return;
        }
        if(intermediate.getFinancialChange() != null)
        {
            fillingAccountChange.balanceChange(basicInformationMessage,
                    sendMessage,
                    intermediateData
            );

            return;
        }

        if(intermediate.getThoughtUser() != null && sendMessage.getText() == null)
        {
            fillingThoughtUser.addTagsAndEmotions(basicInformationMessage,
                    sendMessage,
                    intermediateData
            );

            return;
        }
    }
}
