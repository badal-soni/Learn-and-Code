package com.itt.atm.exception;

public class DailyLimitExceededException extends RuntimeException {

    public DailyLimitExceededException(String message) {
        super(message);
    }

}
