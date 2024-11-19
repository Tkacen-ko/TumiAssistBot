package com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.repository.finance.general.CurrencyRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.FinancialCommandHandler;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.CommandConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.TitleButtonConstant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ButtonReaction {

    private final CurrencyRepository currencyRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final AccountTypeRepository accountTypeRepository;
    private final FinancialCommandHandler financialCommandHandler;

    ButtonReaction(CurrencyRepository currencyRepository,
                   ButtonsBuilder buttonsBuilder,
                   AccountTypeRepository accountTypeRepository,
                   FinancialCommandHandler financialCommandHandler)
    {

        this.currencyRepository = currencyRepository;
        this.buttonsBuilder = buttonsBuilder;
        this.accountTypeRepository = accountTypeRepository;
        this.financialCommandHandler = financialCommandHandler;
    }

    public void buttonProcessing(BasicInformationMessage basicInformationMessage,
                                        SendMessage sendMessage,
                                        Map<String, Intermediate> intermediateData)
    {
        String chatId = basicInformationMessage.getUserTelegram().getChatId().toString();
        String answerText = "Неизвестная кнопка";
        String textMessage = basicInformationMessage.getMessageText();
        InlineKeyboardMarkup keyboardMarkup = null;
        Map<String, String> buttonData = new LinkedHashMap<>();

        if(textMessage.equals(CommandConstant.CANCEL_CREATION_COMMANDS))
        {
            intermediateData.get(chatId).clearData();
            answerText = TitleButtonConstant.CANCEL_CREATION_TITLE;
            return;
        }
        else if (textMessage.equals(ButtonConstant.ADD_FINANCIAL_ACCOUNT_COMMANDS)) {
            financialCommandHandler.getListFinancialOrganizationsAvailableNewAccount(sendMessage);

            return;
        }
        else if(textMessage.contains(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS))
        {
            String firstPartText = textMessage.substring(1, textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS));
            String secondPartText = textMessage.substring(textMessage.lastIndexOf(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS) + 1);

            if(firstPartText.equals(StringConstant.ACCOUNT_TYPE))
            {
                Optional<AccountType> accountType = accountTypeRepository.findByTitle(secondPartText);
                if (!accountType.isEmpty()) {
                    intermediateData.get(chatId).getFinancialAccount().setAccountType(accountType.get());
                }
                for (String currency : currencyRepository.findAllTitles())
                {
                    String commandCode = StringConstant.BASIC_COMMAND_SEPARATOR +
                            String.join(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS,
                                    new String[]{ StringConstant.CURRENCY, currency });
                    buttonData.put(currency, commandCode);
                }
                buttonData.putAll(ButtonConstant.CANCEL_CREATION);

                keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
                sendMessage.setReplyMarkup(keyboardMarkup);

                return;
            }
            if(firstPartText.equals(StringConstant.CURRENCY))
            {
                Optional<Currency> currency = currencyRepository.findByTitle(secondPartText);
                if (!currency.isEmpty()) {
                    intermediateData.get(chatId).getFinancialAccount().setCurrency(currency.get());
                }
            }
        }

        sendMessage.setText(answerText);
    }
}
