package com.tkachenko.BasicTelegramBot.dto.tg.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PreviousMessages {
    String massageId;
    String massageText;
}
