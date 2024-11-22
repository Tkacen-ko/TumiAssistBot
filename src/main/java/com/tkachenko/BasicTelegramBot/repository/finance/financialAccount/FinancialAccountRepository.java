package com.tkachenko.BasicTelegramBot.repository.finance.financialAccount;

import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
    @Query("SELECT fa FROM FinancialAccount fa JOIN FETCH fa.currency " +
            "JOIN FETCH fa.financialOrganization WHERE fa.id = :id")
    Optional<FinancialAccount> findByIdWithCurrencyAndOrganization(@Param("id") Long id);
}