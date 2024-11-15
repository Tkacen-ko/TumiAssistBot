package com.tkachenko.BasicTelegramBot.service.tg.message;

import com.tkachenko.BasicTelegramBot.dto.BasicTelegramData;
import com.tkachenko.BasicTelegramBot.model.finance.Account;
import com.tkachenko.BasicTelegramBot.repository.finance.AccountRepository;
import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ReactionToMessages {
    Map<String, Account> temporaryAccounts = new HashMap<>();

    private final CommandReaction commandReaction;
    private final ButtonReaction buttonReaction;
    private final ButtonsBuilder buttonsBuilder;
    private final AccountRepository accountRepository;

    @Autowired
    public ReactionToMessages(CommandReaction commandReaction,
                              ButtonReaction buttonReaction,
                              ButtonsBuilder buttonsBuilder,
                              AccountRepository accountRepository)
    {
        this.commandReaction = commandReaction;
        this.buttonReaction = buttonReaction;
        this.buttonsBuilder = buttonsBuilder;
        this.accountRepository = accountRepository;
    }

    public SendMessage answerSelection(BasicTelegramData basicTelegramData, SendMessage answererMessage)
    {
        String answerText = ConstantTgBot.BASIC_MASSAGE;
        String textMessage = basicTelegramData.getMassageText();
        answererMessage.setText("Default  text");


        if(textMessage.matches(ConstantTgBot.PATTERN_BUDGET_MESSAGE))
        {
            answerText = "Сообщение бюджетного характера";
        }
        else if(textMessage.charAt(0) == '@')
        {
            SendMessage sendMessage = buttonReaction.buttonProcessing(
                    basicTelegramData,
                    textMessage.substring(1),
                    temporaryAccounts,
                    answererMessage
            );
            Account account = temporaryAccounts.get(basicTelegramData.getIdChat());

            Boolean allFieldsNotNull = Stream.of(account.getTitle(),
                    account.getTotalMoney(), account.getType(),
                    account.getClassification(),account.getOrganization(),
                    account.getCountry()).allMatch(Objects::nonNull);
            if(allFieldsNotNull)
            {
                accountRepository.save(account);
                temporaryAccounts.clear();
                answerText = "Все данные счёта заполнены и счёт сохранён";
                sendMessage.setText(answerText);
            }

            return sendMessage;
        }
        else if(temporaryAccounts.get(basicTelegramData.getIdChat()) != null)
        {
            if(temporaryAccounts.get(basicTelegramData.getIdChat()).getTitle() == null)
            {
                temporaryAccounts.get(basicTelegramData.getIdChat()).setTitle(basicTelegramData.getMassageText());
                answererMessage.setText("Верно ли я понял что имя актива: \n" +
                        basicTelegramData.getMassageText() +
                        "\nЕсли всё верно, наэниме \"Продолжить\" иннициализацию актива" +
                        "\nЕсли данное имя актива не верное, кликните \"Отмена\" и поробуйте ещё раз");
            }
            else if(temporaryAccounts.get(basicTelegramData.getIdChat()).getTotalMoney() == null)
            {
                temporaryAccounts.get(basicTelegramData.getIdChat()).setTotalMoney(Double.parseDouble(basicTelegramData.getMassageText()));
                answererMessage.setText("Верно ли я понял что сумма для хранения равна: \n" +
                        basicTelegramData.getMassageText() +
                        "\nЕсли всё верно, нажмите \"Продолжить\" иннициализацию актива" +
                        "\nЕсли данное имя актива не верное, кликните \"Отмена\" и поробуйте ещё раз");
            }


            Map<String, String> buttonData =  Map.of(
                    "Продолжить иннициализацию актива", "@continue_initializing_account",
                    "Отмена создания", "@cancel_creation"
            );
            InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);

            answererMessage.setReplyMarkup(keyboardMarkup);

            return answererMessage;
        }
        else if(textMessage.charAt(0) == '/')
        {
            return commandReaction.commandProcessing(
                    basicTelegramData,
                    textMessage.substring(1),
                    temporaryAccounts,
                    answererMessage
            );
        }
        answererMessage.setText(answerText);

        return answererMessage;
    }
}
