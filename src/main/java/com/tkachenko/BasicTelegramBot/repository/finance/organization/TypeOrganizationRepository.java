package com.tkachenko.BasicTelegramBot.repository.finance.organization;

import com.tkachenko.BasicTelegramBot.model.finance.organization.TypeOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface TypeOrganizationRepository extends JpaRepository<TypeOrganization, Long> {
    Optional<TypeOrganization> getByTitle(String title);

    @Query("SELECT o.title FROM TypeOrganization o")
    Collection<String> findAllTitles();
}