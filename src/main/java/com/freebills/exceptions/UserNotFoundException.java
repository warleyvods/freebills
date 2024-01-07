package com.freebills.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private static final String MSG = "user not found!";

    public UserNotFoundException() {
        super(MSG);
    }
}
