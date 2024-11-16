package com.tkachenko.BasicTelegramBot.model.finance.organization;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_type")
@NoArgsConstructor
public class AccountType extends GeneralData {

}
