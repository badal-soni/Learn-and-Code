package com.itt.atm.validation;

import com.itt.atm.constant.Constant;
import com.itt.atm.entity.Atm;
import com.itt.atm.entity.BankAccount;
import com.itt.atm.exception.CardBlockedException;
import com.itt.atm.exception.DailyLimitExceededException;

import java.util.Objects;

public class Validator {

    public static void validateAccount(BankAccount account, Atm atm) {
        if (Objects.isNull(account) || Objects.isNull(atm)) {
            throw new RuntimeException(Constant.SERVER_UNAVAILABLE);
        }

        if (account.isBlocked()) {
            throw new CardBlockedException(Constant.CARD_BLOCKED);
        }
    }

    public static void validateDailyLimit(long amount) {
        if (amount > Constant.DAILY_LIMIT) {
            throw new DailyLimitExceededException(Constant.LIMIT_EXCEEDED);
        }
    }

}
