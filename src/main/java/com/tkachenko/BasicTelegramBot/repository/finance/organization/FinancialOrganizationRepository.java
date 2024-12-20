package com.tkachenko.BasicTelegramBot.repository.finance.organization;

import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialOrganizationRepository extends JpaRepository<FinancialOrganization, Long> {

    Optional<FinancialOrganization> findByTitle(String title);
    Optional<FinancialOrganization> findById(Integer id);
    Optional<FinancialOrganization> findByShortName(String shortName);
}
