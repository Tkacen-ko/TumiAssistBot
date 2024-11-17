package com.tkachenko.BasicTelegramBot.model.finance.organization;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country")
@NoArgsConstructor
public class Country extends GeneralData {
    public Country(Long id, String title) {
        super(id, title);
    }
}