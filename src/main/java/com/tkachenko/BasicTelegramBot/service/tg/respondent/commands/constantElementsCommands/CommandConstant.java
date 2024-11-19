package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands;

import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;

import java.util.Map;

public class CommandConstant {
    public static final String CANCEL_CREATION_COMMANDS =
            StringConstant.BASIC_COMMAND_SEPARATOR + String.join(ConstantTgBot.BASIC_SEPARATOR_FOR_COMMANDS, new String[]{
                    StringConstant.CANCEL, "creation"
            });
}