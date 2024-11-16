package com.tkachenko.BasicTelegramBot.repository.finance.organization;

import com.tkachenko.BasicTelegramBot.model.finance.organization.Country;
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
