package com.freebills.gateways.entities.json;

public record TransactionMetadata(
        boolean bankSlip,
        boolean observation
) {
    public TransactionMetadata() {
        this(false, false);
    }
}
