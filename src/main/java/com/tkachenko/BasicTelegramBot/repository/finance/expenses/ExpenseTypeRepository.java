package com.tkachenko.BasicTelegramBot.repository.finance.expenses;

import com.tkachenko.BasicTelegramBot.model.finance.expenses.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {
    Optional<ExpenseType> findByShortName(String shortName);
    Optional<ExpenseType> findByTitle(String title);
}
