package com.itt.atm.service;

import com.itt.atm.constant.Constant;
import com.itt.atm.dto.request.WithDrawMoneyRequest;
import com.itt.atm.dto.response.WithDrawMoneyResponse;
import com.itt.atm.entity.Atm;
import com.itt.atm.entity.BankAccount;
import com.itt.atm.exception.AttemptsExhaustedException;
import com.itt.atm.exception.InSufficientBalance;
import com.itt.atm.repository.AtmRepository;
import com.itt.atm.repository.BankAccountRepository;
import com.itt.atm.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtmService {

    private final BankAccountRepository accountRepository;
    private final AtmRepository atmRepository;

    public WithDrawMoneyResponse withDrawAmount(WithDrawMoneyRequest request) {
        Atm atm = atmRepository.findAtm(request.getAtmId());
        BankAccount account = accountRepository.findAccount(UUID.fromString(request.getAccountId()));

        Validator.validateAccount(account, atm);
        verifyPin(account, request.getPin());

        account.setInvalidAttempts(0);
        Validator.validateDailyLimit(account.getDailyWithdrawn() + request.getAmount());

        if (atm.getBalance() < request.getAmount() || account.getBalance() < request.getAmount()) {
            throw new InSufficientBalance(Constant.INSUFFICIENT_AMOUNT);
        }

        atm.setBalance(atm.getBalance() - request.getAmount());
        account.setBalance(account.getBalance() - request.getAmount());
        account.setDailyWithdrawn(account.getDailyWithdrawn() + request.getAmount());

        atmRepository.updateAtm(atm);
        accountRepository.updateAccount(account);

        return new WithDrawMoneyResponse(account.getBalance());
    }

    private void verifyPin(BankAccount account, String pin) {
        if (!account.getPin().equals(pin)) {
            account.setInvalidAttempts(account.getInvalidAttempts() + 1);
            if (account.getInvalidAttempts() > Constant.MAX_INVALID_ATTEMPTS) {
                account.setBlocked(true);
                account.setInvalidAttempts(0);
                accountRepository.updateAccount(account);
                throw new AttemptsExhaustedException(Constant.ATTEMPTS_EXHAUSTED);
            }
            accountRepository.updateAccount(account);
        }
    }

}
