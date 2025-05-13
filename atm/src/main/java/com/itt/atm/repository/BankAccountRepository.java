package com.itt.atm.repository;

import com.itt.atm.entity.Atm;
import com.itt.atm.entity.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class BankAccountRepository {

    private static final Map<UUID, Atm> atms = new HashMap<>();
    private static final Map<UUID, BankAccount> accounts = new HashMap<>();

    public BankAccount findAccount(UUID accountId) {
        return accounts.get(accountId);
    }

    public void updateAccount(BankAccount account) {
        accounts.put(account.getId(), account);
    }

}
