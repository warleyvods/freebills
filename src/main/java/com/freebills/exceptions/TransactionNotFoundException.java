package com.freebills.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(final String msg) {
        super(msg);
    }
}
