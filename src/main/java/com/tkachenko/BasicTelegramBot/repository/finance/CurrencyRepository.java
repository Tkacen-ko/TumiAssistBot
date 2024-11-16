package com.tkachenko.BasicTelegramBot.repository.finance;

import com.tkachenko.BasicTelegramBot.model.finance.Country;
import com.tkachenko.BasicTelegramBot.model.finance.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> getByTitle(String title);

    @Query("SELECT o.title FROM Currency o")
    Collection<String> findAllTitles();
}
