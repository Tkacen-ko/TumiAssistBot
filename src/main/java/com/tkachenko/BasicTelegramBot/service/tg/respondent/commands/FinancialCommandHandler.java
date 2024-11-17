package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.CommandConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.StringConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FinancialCommandHandler {
    private final FinancialOrganizationRepository financialOrganizationRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final UserRepository userRepository;

    @Autowired
    FinancialCommandHandler(FinancialOrganizationRepository financialOrganizationRepository,
                            ButtonsBuilder buttonsBuilder,
                            UserRepository userRepository)
    {
        this.financialOrganizationRepository = financialOrganizationRepository;
        this.buttonsBuilder = buttonsBuilder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addFinancialOrganizationsUser(SendMessage sendMessage,
                                              Long chatId,
                                              BasicInformationMessage basicInformationMessage,
                                              Map<String, Intermediate> intermediateData) {
        List<FinancialOrganization> allFinancialOrganizationByUser =
                financialOrganizationRepository.findAllByUserChatId(chatId);
        String textMessage = basicInformationMessage.getMessageText();
        InlineKeyboardMarkup keyboardMarkup = null;
        Map<String, String> buttonData = new LinkedHashMap<>();
        boolean isEmptyAllFinancialOrganizationByUser = allFinancialOrganizationByUser.isEmpty();
        boolean previousMessageChosenFinancialAccount = basicInformationMessage.getHistoryReceivedMessages().get(1).getMessageText().equals(CommandConstant.ADD_FINANCIAL_ACCOUNT_COMMANDS);
        boolean previousMessageUserMistakeChosenFinancialAccount = basicInformationMessage.getHistorySentMessages().get(0).equals(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);

        if(isEmptyAllFinancialOrganizationByUser && (previousMessageChosenFinancialAccount || previousMessageUserMistakeChosenFinancialAccount))
        {
            List<Integer> listNumberIdCompany = parseNumbers(textMessage, sendMessage);
            if(listNumberIdCompany.isEmpty() || sendMessage.getText() != null)
            {
                sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
                return;
            }
            List<FinancialOrganization> financialOrganizations = financialOrganizationRepository.findAll();
            List<FinancialOrganization> userSelectedOrganizations = new ArrayList<>();
            for(Integer currentIndexCompany : listNumberIdCompany)
            {
                try
                {
                    userSelectedOrganizations.add(financialOrganizations.get(currentIndexCompany - 1));
                }
                catch (IndexOutOfBoundsException e) {
                    sendMessage.setText(StringConstant.INVALID_COMPANY_NUMBER);
                    return;
                }
            }
            Optional<UserTelegram> currentUser = userRepository.findByChatId(chatId);
            currentUser.get().getFinancialOrganizations().addAll(userSelectedOrganizations);
            userRepository.save(currentUser.get());
            allFinancialOrganizationByUser.addAll(userSelectedOrganizations);
            isEmptyAllFinancialOrganizationByUser = false;
            intermediateData.get(basicInformationMessage.getUserTelegram().getChatId().toString())
                    .getFinancialAccount().setFinancialOrganizations(userSelectedOrganizations);
            sendMessage.setText(StringConstant.LIST_FINANCIAL_COMPANIES_ESTABLISHED);
        }
        else if (isEmptyAllFinancialOrganizationByUser) {
            List<FinancialOrganization> financialOrganizations = financialOrganizationRepository.findAll();
            String textNewMessage = IntStream.range(0, financialOrganizations.size())
                    .mapToObj(i -> String.format(
                            "%d. %s (%s), %s.",
                            i + 1, // Нумерация начинается с 1
                            financialOrganizations.get(i).getTitle(),
                            financialOrganizations.get(i).getCountry().getTitle(),
                            financialOrganizations.get(i).getTypeOrganization().getTitle()
                    ))
                    .collect(Collectors.joining("\n", "", ""));

            String allTextMessage = StringConstant.INTRODUCTORY_TEXT_SELECTION_COMPANIES + textNewMessage;
            sendMessage.setText(allTextMessage);
            buttonData.putAll(ButtonConstant.CANCEL_CREATION);
            keyboardMarkup = buttonsBuilder.createMessageWithButtons(ButtonConstant.CANCEL_CREATION);
        }

        if (!isEmptyAllFinancialOrganizationByUser && textMessage.charAt(0) != '@')
        {
            for (FinancialOrganization financialOrganization : allFinancialOrganizationByUser)
            {
                String codeButton = StringConstant.BASIC_COMMAND_SEPARATOR +
                        StringConstant.FIRST_PART_ORGANIZATION_SELECTION_COMMAND +
                        financialOrganization.getTitle();

                buttonData.put(financialOrganization.getTitle(), codeButton);
                if(sendMessage.getText() == null)
                {
                    sendMessage.setText(StringConstant.SELECT_ORGANIZATION_OPEN_ACCOUNT);
                }
            }
            buttonData.putAll(ButtonConstant.CANCEL_CREATION);
            keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);

        }
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private List<Integer> parseNumbers(String input, SendMessage sendMessage) {
        List<Integer> numbers = new ArrayList<>();

        if (input == null || input.isBlank()) {
            sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
            return numbers;
        }
        String[] parts = input.split(",");

        for (String part : parts) {
            try {
                int number = Integer.parseInt(part.trim());
                numbers.add(number);
            } catch (NumberFormatException e) {
                sendMessage.setText(StringConstant.INCORRECT_FORMAT_SELECTING_ORGANIZATION);
            }
        }

        return numbers;
    }

    public void setFinanceOrganization()
    {

    }
}
