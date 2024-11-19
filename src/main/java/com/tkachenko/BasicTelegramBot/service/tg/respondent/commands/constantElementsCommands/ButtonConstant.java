package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands;

import java.util.Map;

public class ButtonConstant {
    public static final String ADD_FINANCIAL_ACCOUNT_COMMANDS = "@addFinancialAccount";
    public static final Map<String, String> CANCEL_CREATION = Map.of(
            TitleButtonConstant.CANCEL_CREATION_TITLE, CommandConstant.CANCEL_CREATION_COMMANDS
    );
}
