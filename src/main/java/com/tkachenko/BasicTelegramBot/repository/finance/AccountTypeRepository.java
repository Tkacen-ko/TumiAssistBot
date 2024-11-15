package com.tkachenko.BasicTelegramBot.repository.finance;

import com.tkachenko.BasicTelegramBot.model.finance.AccountType;
import com.tkachenko.BasicTelegramBot.model.finance.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> getByTitle(String title);

    @Query("SELECT t.title FROM AccountType t")
    Collection<String> findAllTitles();
}
