package com.freebills.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(final String s) {
        super(s);
    }
}
