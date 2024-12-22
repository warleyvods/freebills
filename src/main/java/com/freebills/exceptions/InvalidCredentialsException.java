package com.freebills.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    private static final String MSG = "invalid login or password";

    public InvalidCredentialsException() {
        super(MSG);
    }
}
