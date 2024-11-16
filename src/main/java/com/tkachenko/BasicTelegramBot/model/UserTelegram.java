package com.tkachenko.BasicTelegramBot.model;

import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "user_telegram")
@NoArgsConstructor
public class UserTelegram {

    public UserTelegram(Long chatId,
                        String firstName,
                        String lastName,
                        String userName) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;

    @ManyToMany
    @JoinTable(
            name = "user_organization",
            joinColumns = @JoinColumn(name = "user_telegram_id"),
            inverseJoinColumns = @JoinColumn(name = "financial_organization_id")
    )
    private List<FinancialOrganization> financialOrganizations;
}