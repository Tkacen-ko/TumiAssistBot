package com.tkachenko.BasicTelegramBot.model.finance.expenses;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expense_type")
@NoArgsConstructor
public class ExpenseType extends GeneralData {
    public ExpenseType(Long id,String title, String shortName) {
        super(id, title);
        this.shortName = shortName;
    }
    @Column(name = "short_name", nullable = false, length = 50)
    private String shortName;
}

