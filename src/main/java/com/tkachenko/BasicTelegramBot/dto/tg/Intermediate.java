package com.tkachenko.BasicTelegramBot.dto.tg;

import com.tkachenko.BasicTelegramBot.model.finance.expenses.FinancialChange;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import lombok.Data;

@Data
public class Intermediate {
    FinancialAccount financialAccount;
    FinancialChange financialChange;

    public void clearData()
    {
        financialAccount = null;
        financialChange = null;
    }

    public boolean checkData()
    {
        return this.financialAccount != null ||
                this.financialChange != null;
    }
}
