package com.tkachenko.BasicTelegramBot.model.finance;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expense_type")
@NoArgsConstructor
public class ExpenseType extends GeneralData{

}

