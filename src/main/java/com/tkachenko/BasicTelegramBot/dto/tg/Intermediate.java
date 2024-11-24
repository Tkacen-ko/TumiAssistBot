package com.tkachenko.BasicTelegramBot.dto.tg;

import com.tkachenko.BasicTelegramBot.model.finance.expenses.FinancialChange;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.Emotion;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.ThoughtUser;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.UserEmotion;
import lombok.Data;

import java.util.List;

@Data
public class Intermediate {
    FinancialAccount financialAccount;
    FinancialChange financialChange;
    ThoughtUser thoughtUser;
    List<UserEmotion> userEmotions;
    List<Emotion> emotions;

    public void clearData()
    {
        financialAccount = null;
        financialChange = null;
        thoughtUser = null;
        userEmotions = null;
        emotions = null;
    }

    public boolean checkData()
    {
        return this.financialAccount != null ||
                this.financialChange != null ||
                this.thoughtUser != null ||
                this.userEmotions != null ||
                this.emotions != null;
    }
}
