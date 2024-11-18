package com.tkachenko.BasicTelegramBot.dto.tg.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessagesHistoryData {
    String messageId;
    String messageText;
    Boolean isButton;
}
