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
    TypeOrganization typeOrganization;
    Country country;

    @ManyToMany(mappedBy = "financialOrganizations")
    private List<FinancialAccount> financialAccounts;

    @ManyToMany(mappedBy = "financialOrganizations")
    private List<UserTelegram> users;
}