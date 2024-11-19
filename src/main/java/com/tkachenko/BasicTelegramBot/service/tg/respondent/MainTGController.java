package com.tkachenko.BasicTelegramBot.service.tg.respondent;

import java.util.*;
import java.util.stream.Stream;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.FinancialCommandHandler;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonReaction;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.CommandReaction;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.intermediateData.IntermediateProcessing;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MainTGController {
    /**
     * Основаня переменнная для промежуточного хранения данных между запросами из бота
     */
    private final Map<String, Intermediate> intermediateData = new HashMap<>();

    private final CommandReaction commandReaction;
    private final ButtonReaction buttonReaction;
    private final IntermediateProcessing intermediate;
    private final FinancialCommandHandler financialCommandHandler;

    @Autowired
    public MainTGController(CommandReaction commandReaction,
                            ButtonReaction buttonReaction,
                            IntermediateProcessing intermediate,
                            FinancialCommandHandler financialCommandHandler)
    {
        this.commandReaction = commandReaction;
        this.buttonReaction = buttonReaction;
        this.intermediate = intermediate;
        this.financialCommandHandler = financialCommandHandler;
    }

    /**
     * Метод выбора ответа на сообщение пользователя
     * @param basicInformationMessage - базовые информация сообщения
     * @param sendMessage - сообщение для отправки пользователю
     * @return сообщение для пользователя
     */
    public SendMessage answerSelection(BasicInformationMessage basicInformationMessage,
                                       SendMessage sendMessage)
    {
        String textMessage = basicInformationMessage.getMessageText();
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        intermediateData.putIfAbsent(chatId, new Intermediate());
        char firstSymbolTextMessage = textMessage.charAt(0);

        if(firstSymbolTextMessage == '/')
        {
            commandReaction.commandProcessing(basicInformationMessage, sendMessage, intermediateData);
        }
        else if(firstSymbolTextMessage == '@')
        {
            buttonReaction.buttonProcessing(basicInformationMessage, sendMessage, intermediateData);
        }
        else if(firstSymbolTextMessage == '-' || firstSymbolTextMessage == '+')
        {
            //TODO logic adding new consumption
        }
        else if(intermediateData.get(chatId).checkData())
        {
            intermediate.checkingForIntermediateData(basicInformationMessage, sendMessage, intermediateData);
        }

        if(sendMessage.getText() == null)
        {
            sendMessage.setText(ConstantTgBot.BASIC_MESSAGE);
        }

        return sendMessage;
    }

    void checkAgainstPreviouslySentMessages(BasicInformationMessage basicInformationMessage,
                                            SendMessage sendMessage,
                                            Map<String, Intermediate> intermediateData)
    {
        if(!basicInformationMessage.getHistorySentMessages().isEmpty())
        {
            String textPreviousSentMessage = basicInformationMessage.getHistorySentMessages().getFirst().getMessageText();
            if(textPreviousSentMessage.contains(StringConstant.INTRODUCTORY_TEXT_SELECTION_COMPANIES.replaceAll("[*_`~]", "")));
            {
                financialCommandHandler.saveNewUserAccount(sendMessage, basicInformationMessage, intermediateData);
            }
            if(textPreviousSentMessage.contains(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION))
            {
                sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
                financialCommandHandler.getListFinancialOrganizationsAvailableNewAccount(sendMessage);
            }

        }
        if(sendMessage.getText() == null)
        {
            sendMessage.setText(ConstantTgBot.BASIC_MESSAGE);
        }
    }
}
