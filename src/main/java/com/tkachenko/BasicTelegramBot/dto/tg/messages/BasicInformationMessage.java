package com.tkachenko.BasicTelegramBot.dto.tg.messages;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.utils.LimitedSizeMessageList;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BasicInformationMessage {
    UserTelegram userTelegram;
    String massageText;
    LimitedSizeMessageList<PreviousMessages> limitedSizeMessageList;
}
