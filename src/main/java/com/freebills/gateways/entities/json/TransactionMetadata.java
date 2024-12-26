package com.freebills.gateways.entities.json;

public record TransactionMetadata(
        boolean hasBankSlip,
        boolean hasObservation,
        String receipt
) { }
