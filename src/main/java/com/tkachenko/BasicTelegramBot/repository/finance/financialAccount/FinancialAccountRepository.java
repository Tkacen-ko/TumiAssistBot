package com.tkachenko.BasicTelegramBot.repository.finance.financialAccount;

import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
}