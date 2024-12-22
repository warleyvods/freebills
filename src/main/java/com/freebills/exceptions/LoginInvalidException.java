package com.freebills.exceptions;

public class LoginInvalidException extends RuntimeException {
    public LoginInvalidException(String msg) {
        super(msg);
    }
}
