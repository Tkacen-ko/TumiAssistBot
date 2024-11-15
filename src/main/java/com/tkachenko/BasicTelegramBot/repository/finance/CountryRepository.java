package com.tkachenko.BasicTelegramBot.repository.finance;

import com.tkachenko.BasicTelegramBot.model.finance.Classification;
import com.tkachenko.BasicTelegramBot.model.finance.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> getByTitle(String title);

    @Query("SELECT o.title FROM Country o")
    Collection<String> findAllTitles();
}
