package com.itt.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount extends BaseEntity {

    private String pin;
    private long balance;
    private int invalidAttempts;
    private long dailyWithdrawn;
    private boolean isBlocked;

}
