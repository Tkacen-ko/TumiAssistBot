package com.tkachenko.BasicTelegramBot.model.finance.expenses;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "financial_change")
public class FinancialChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    private UserTelegram user;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false, foreignKey = @ForeignKey(name = "fk_currency"))
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "expense_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_type"))
    private ExpenseType expenseType;
}
