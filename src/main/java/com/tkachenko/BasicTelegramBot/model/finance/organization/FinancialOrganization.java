package com.tkachenko.BasicTelegramBot.model.finance.organization;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "financial_organization")
@NoArgsConstructor
@Data
public class FinancialOrganization extends GeneralData {
    public FinancialOrganization(Long id, String title, TypeOrganization typeOrganization, Country country, String shortName) {
        super(id, title);
        this.typeOrganization = typeOrganization;
        this.country = country;
        this.shortName = shortName;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_organization_id", nullable = false)
    private TypeOrganization typeOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "short_name", nullable = false, length = 50)
    private String shortName;

    @ManyToMany(mappedBy = "financialOrganizations")
    private List<FinancialAccount> financialAccounts;
}