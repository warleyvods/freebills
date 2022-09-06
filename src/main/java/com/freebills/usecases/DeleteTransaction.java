package com.freebills.usecases;

import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

@Component
public record DeleteTransaction(TransactionGateway transactionGateway) {

    public void delete(final Long id) {
        transactionGateway.delete(id);
    }
}
