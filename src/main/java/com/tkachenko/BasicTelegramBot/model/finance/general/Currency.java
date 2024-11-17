package com.tkachenko.BasicTelegramBot.model.finance.general;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currency")
@AllArgsConstructor
@Data
public class Currency extends GeneralData {
    public Currency(Long id, String title) {
        super(id, title);
    }
}