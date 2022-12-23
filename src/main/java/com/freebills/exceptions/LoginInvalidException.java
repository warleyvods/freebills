package com.freebills.exceptions;

public class LoginInvalidException extends RuntimeException {
    public LoginInvalidException(String s) {
        super(s);
    }
}
