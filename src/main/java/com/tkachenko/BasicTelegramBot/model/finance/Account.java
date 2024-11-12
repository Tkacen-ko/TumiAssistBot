package com.tkachenko.BasicTelegramBot.model.finance;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTelegram user; // Ссылка на пользователя

    @Column(name = "title", nullable = false)
    private String title; // Название позиции

    @Column(name = "total_money")
    private Double totalMoney; // Общая сумма на счете

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization; // Ссылка на организацию

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AccountType type; // Ссылка на тип счета

    @ManyToOne
    @JoinColumn(name = "classification_id", nullable = false)
    private Classification classification; // Ссылка на классификацию

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country; // Ссылка на страну
}