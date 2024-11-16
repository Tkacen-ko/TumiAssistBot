package com.tkachenko.BasicTelegramBot.service.tg.respondent.builder;

import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class FinancialAccountFilling {

    private final FinancialOrganizationRepository financialOrganizationRepository;

    @Autowired
    FinancialAccountFilling(FinancialOrganizationRepository financialOrganizationRepository)
    {
        this.financialOrganizationRepository = financialOrganizationRepository;
    }

    public void checkFillingDataFinancialAccount(BasicInformationMessage basicInformationMessage,
                                                 SendMessage sendMessage,
                                                 FinancialAccount financialAccount) {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        boolean financialOrganizationsIsEmpty = financialAccount.getFinancialOrganizations().isEmpty();
        basicInformationMessage.getLimitedSizeMessageList().get(1).getMassageText().equals();

        if (financialOrganizationsIsEmpty && )
        {
            List<FinancialOrganization> allFinancialOrganizationByUser =
                    financialOrganizationRepository.findAllByUserChatId(chatId);

        }
        if (financialAccount.getTitle() == null)
        {
            List<FinancialOrganization> allFinancialOrganizationByUser = financialOrganizationRepository.findAllByUserChatId(basicInformationMessage.getUserTelegram().getChatId());

        }

        if (financialAccount.getAccountType() == null)
        {

        }

        if (financialAccount.getAccountType() == null)
        {

        }



        //TODO хлам с прошлой реализации
//        intermediateData.get(basicInformationMessage.getIdChat()).setTitle(basicInformationMessage.getMassageText());
//        sendMessage.setText("Верно ли я понял что имя актива: \n" +
//                basicInformationMessage.getMassageText() +
//                "\nЕсли всё верно, наэниме \"Продолжить\" иннициализацию актива" +
//                "\nЕсли данное имя актива не верное, кликните \"Отмена\" и поробуйте ещё раз");
//
//        if(intermediateData.get(basicInformationMessage.getIdChat()).getTotalMoney() == null)
//        {
//            intermediateData.get(basicInformationMessage.getIdChat()).setTotalMoney(Float.parseFloat(basicInformationMessage.getMassageText()));
//            sendMessage.setText("Верно ли я понял что сумма для хранения равна: \n" +
//                    basicInformationMessage.getMassageText() +
//                    "\nЕсли всё верно, нажмите \"Продолжить\" иннициализацию актива" +
//                    "\nЕсли данное имя актива не верное, кликните \"Отмена\" и поробуйте ещё раз");
//        }
//
//
//        Map<String, String> buttonData =  Map.of(
//                "Продолжить иннициализацию актива", "@continue_initializing_account",
//                "Отмена создания", "@cancel_creation"
//        );
//        InlineKeyboardMarkup keyboardMarkup = buttonsBuilder.createMessageWithButtons(buttonData);
//
//        sendMessage.setReplyMarkup(keyboardMarkup);
    }
}
