package com.tkachenko.BasicTelegramBot.model.finance.financialAccount;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "financial_account")
@NoArgsConstructor
@Data
public class FinancialAccount extends GeneralData {
    @Column(name = "balance", nullable = false)
    private Double balance = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @ManyToMany
    @JoinTable(
            name = "financial_account_organization",
            joinColumns = @JoinColumn(name = "financial_account_id"),
            inverseJoinColumns = @JoinColumn(name = "financial_organization_id")
    )
    private List<FinancialOrganization> financialOrganizations;
}
