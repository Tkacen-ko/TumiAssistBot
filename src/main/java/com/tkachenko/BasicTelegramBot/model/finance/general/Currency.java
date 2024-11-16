package com.tkachenko.BasicTelegramBot.model.finance.general;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currency")
@NoArgsConstructor
public class Currency extends GeneralData {

}