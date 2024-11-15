package com.tkachenko.BasicTelegramBot.repository.finance;

import com.tkachenko.BasicTelegramBot.model.finance.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> getByTitle(String title);

    @Query("SELECT o.title FROM Organization o")
    Collection<String> findAllTitles();
}