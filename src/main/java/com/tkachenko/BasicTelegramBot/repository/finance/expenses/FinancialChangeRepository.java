package com.tkachenko.BasicTelegramBot.repository.finance.expenses;

import com.tkachenko.BasicTelegramBot.model.finance.expenses.FinancialChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialChangeRepository extends JpaRepository<FinancialChange, Long> {
}
