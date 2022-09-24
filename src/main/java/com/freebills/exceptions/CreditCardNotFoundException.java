package com.freebills.exceptions;

public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(final String s) {
        super(s);
    }
}
