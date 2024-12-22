package com.freebills.exceptions;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(final String msg) {
        super(msg);
    }
}
