package com.tkachenko.BasicTelegramBot.model.finance.organization;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type_organization")
@NoArgsConstructor
public class TypeOrganization  extends GeneralData {
    public TypeOrganization(Long id, String title) {
        super(id, title);
    }
}