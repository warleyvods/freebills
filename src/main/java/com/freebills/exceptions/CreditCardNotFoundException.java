package com.freebills.exceptions;

public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(final String msg) {
        super(msg);
    }
}
