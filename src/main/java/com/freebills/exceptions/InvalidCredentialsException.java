package com.freebills.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {

    private static final String MSG = "invalid login or password";

    public InvalidCredentialsException() {
        super(MSG);
    }
}
