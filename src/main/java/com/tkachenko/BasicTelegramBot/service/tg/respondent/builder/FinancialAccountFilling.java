package com.tkachenko.BasicTelegramBot.service.tg.respondent.builder;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.financialAccount.FinancialAccountRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonReaction;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.FinancialCommandHandler;
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
    private final ButtonReaction buttonReaction;
    private final FinancialAccountRepository financialAccountRepository;

    @Autowired
    FinancialAccountFilling(ButtonReaction buttonReaction,
                            FinancialAccountRepository financialAccountRepository)
    {
        this.buttonReaction = buttonReaction;
        this.financialAccountRepository = financialAccountRepository;
    }

    @Transactional
    public void checkFillingDataFinancialAccount(BasicInformationMessage basicInformationMessage,
                                                 SendMessage sendMessage,
                                                 Map<String, Intermediate> intermediateData) {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        FinancialAccount financialAccount = intermediateData.get(chatId).getFinancialAccount();

        boolean allFieldsNotNull = Stream.of(
                financialAccount.getTitle(),
                financialAccount.getAccountType(), financialAccount.getCurrency()
        ).allMatch(Objects::nonNull);

        if (allFieldsNotNull) {
            financialAccountRepository.save(financialAccount);
            intermediateData.get(chatId).clearData();
            String answerText = "Все данные счёта заполнены и счёт сохранён";
            sendMessage.setText(answerText);
        }
    }
}
