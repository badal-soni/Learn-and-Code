package com.itt.atm.exception;

public class InSufficientBalance extends RuntimeException {

    public InSufficientBalance(String message) {
        super(message);
    }

}
