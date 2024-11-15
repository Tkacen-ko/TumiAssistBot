package com.tkachenko.BasicTelegramBot.repository.finance;

import com.tkachenko.BasicTelegramBot.model.finance.Account;
import com.tkachenko.BasicTelegramBot.model.finance.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
