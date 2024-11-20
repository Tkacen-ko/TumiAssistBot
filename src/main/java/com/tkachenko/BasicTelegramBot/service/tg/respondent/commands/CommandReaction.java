package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;


@Service
public class CommandReaction {
    public SendMessage commandProcessing(BasicInformationMessage basicInformationMessage,
                                         SendMessage sendMessage,
                                         Map<String, Intermediate> intermediateData)
    {
        return sendMessage;
    }
}
