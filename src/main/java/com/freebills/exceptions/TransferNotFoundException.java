package com.freebills.exceptions;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException(String msg) {
        super(msg);
    }
}
