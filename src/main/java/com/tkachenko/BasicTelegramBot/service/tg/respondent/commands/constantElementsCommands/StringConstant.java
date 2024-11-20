package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands;

import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;

public class StringConstant {
    public static final String BASIC_COMMAND_SEPARATOR = "@";
    public static final String SLASH = "/";

    public static final String CANCEL = "cancel";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String CURRENCY = "currency";

    public static final String INTRODUCTORY_TEXT_SELECTION_COMPANIES = "Выберите подходящую компанию из списка ниже и отправьте в чат сообщение с номером выбранного пункта.\n" +
            "Например: \"2\" :\n\n";

    public static final String SELECT_ACCOUNT_TYPE = "Определите тип счёта из предложенных ниже вариантов:";

    public static final String ACCOUNT_TYPE_OK_SELECT_CURRENCY = "Типа аккаунта записан.\nВыберете валюту счёта или нажмите \"Отмана\"";

    public static final String NOT_FINISHED_FILLING_FINANCIAL_ORGANIZATION_SETTINGS = "Вы не закончили заполнение настроек финансовой организации.";

    public static final String SELECT_ACCOUNT_CURRENCY = "Выберете валюту счёта или нажмите \"Отмана\"";

    public static final String INCORRECT_FORMAT_SELECTING_ORGANIZATION = "Неверный формат выбора организации.\n" +
            "Проверьте, направляемоего номера выбранной вами компании и попробуйте написать его ещё раз. Просто число из перечня ниже (Например \"1\"):\n\n";

    public static final String INVALID_COMPANY_NUMBER = "Число выбранной вами компании отсутствует в списке. Проверьте всё ещё раз и попробуйте отправить новый список.\n\n";

    public static final String LIST_FINANCIAL_COMPANIES_ESTABLISHED = "Отлично! Список финансовых компаний установлен! \n";
}
