package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;

@Service
public class CommandReaction {

    private final FinancialOrganizationRepository financialOrganizationRepository;

    @Autowired
    public CommandReaction(FinancialOrganizationRepository financialOrganizationRepository)
    {
        this.financialOrganizationRepository = financialOrganizationRepository;
    }

    public SendMessage commandProcessing(BasicInformationMessage basicInformationMessage,
                                         SendMessage sendMessage,
                                         Map<String, Intermediate> intermediateData)
    {
        String massageText = basicInformationMessage.getMassageText();
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();

        if (massageText.equals(CommandConstant.ADD_FINANCIAL_ACCOUNT_COMMANDS))
        {
            List<FinancialOrganization> allFinancialOrganizationByUser =
                    financialOrganizationRepository.findAllByUserChatId(chatId);
            if(allFinancialOrganizationByUser.isEmpty())
            {

            }
        }

        return sendMessage;
    }
}
