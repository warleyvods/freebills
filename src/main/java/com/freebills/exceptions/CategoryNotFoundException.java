package com.freebills.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(final String msg) {
        super(msg);
    }
}
