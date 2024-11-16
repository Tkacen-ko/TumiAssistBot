package com.tkachenko.BasicTelegramBot.repository.finance.financialAccount;

import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
}