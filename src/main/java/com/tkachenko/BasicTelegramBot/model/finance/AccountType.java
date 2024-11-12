package com.tkachenko.BasicTelegramBot.model.finance;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_type")
@NoArgsConstructor
public class AccountType extends GeneralData{

}
