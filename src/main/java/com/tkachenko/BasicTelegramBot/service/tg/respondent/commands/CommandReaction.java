package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.CommandConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Service
public class CommandReaction {

    private final FinancialOrganizationRepository financialOrganizationRepository;
    private final FinancialCommandHandler financialCommandHandler;

    @Autowired
    public CommandReaction(FinancialOrganizationRepository financialOrganizationRepository,
                           FinancialCommandHandler financialCommandHandler)
    {
        this.financialOrganizationRepository = financialOrganizationRepository;
        this.financialCommandHandler = financialCommandHandler;
    }

    public SendMessage commandProcessing(BasicInformationMessage basicInformationMessage,
                                         SendMessage sendMessage,
                                         Map<String, Intermediate> intermediateData)
    {
        String messageText = basicInformationMessage.getMessageText();
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();

        if (messageText.equals(CommandConstant.ADD_FINANCIAL_ACCOUNT_COMMANDS)) {
            financialCommandHandler.addFinancialOrganizationsUser(sendMessage, chatId, basicInformationMessage, intermediateData);
            intermediateData.get(chatId.toString()).setFinancialAccount(new FinancialAccount());
            return sendMessage;
        }

        return sendMessage;
    }
}
