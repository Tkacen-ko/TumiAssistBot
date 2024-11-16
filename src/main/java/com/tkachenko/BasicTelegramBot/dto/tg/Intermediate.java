package com.tkachenko.BasicTelegramBot.dto.tg;

import com.tkachenko.BasicTelegramBot.model.finance.expenses.FinancialChange;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import lombok.Data;

@Data
public class Intermediate {
    FinancialAccount financialAccount;
    FinancialChange financialChange;
}
