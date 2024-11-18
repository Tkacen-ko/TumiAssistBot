package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands;

import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;

public class StringConstant {
    public static final String BASIC_COMMAND_SEPARATOR = "@";
    public static final String SLASH = "/";

    public static final String CANCEL = "cancel";
    public static final String ORGANIZATION = "organization";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String CURRENCY = "currency";

    public static final String INTRODUCTORY_TEXT_SELECTION_COMPANIES = "У вас не выбраны компании в которых вы разместили средства.\n" +
            "Выберите из списка ниже компаний в которых у вас есть счета и отправься ниже в виде списка \"2, 4, 5,11\"\n" +
            "Строго в таком формате без других символов кроме цифр и запятых. Цифры строго в рамках списка представленного ниже.\n";

    public static final String SELECT_ACCOUNT_TYPE = "Определите тип счёта из предложенных ниже вариантов:";

    public static final String INCORRECT_FORMAT_SELECTING_ORGANIZATION = "Неверный формат выбора организации.\n" +
            "Проверьте, соответствует ли ваше предыдущее сообщение указноому формату.";

    public static final String INVALID_COMPANY_NUMBER = "Число выбранной вами компании отсутствует в списке. Проверьте всё ещё раз и попробуйте отправить новый список.";

    public static final String LIST_FINANCIAL_COMPANIES_ESTABLISHED = "Отлично! Список финансовых компаний установлен! \n";
}
