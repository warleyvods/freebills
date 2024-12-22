package com.freebills.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final String MSG = "user not found!";

    public UserNotFoundException() {
        super(MSG);
    }
}
