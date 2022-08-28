package com.freebills.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(final String msg) {
        super(msg);
    }
}
