package com.itt.atm.exception;

public class AttemptsExhaustedException extends RuntimeException {

    public AttemptsExhaustedException(String message) {
        super(message);
    }

}
