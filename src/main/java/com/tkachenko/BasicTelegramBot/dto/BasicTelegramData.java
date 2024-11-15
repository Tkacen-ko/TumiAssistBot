package com.tkachenko.BasicTelegramBot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BasicTelegramData {
    String idChat;
    String massageText;
}
