package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateTransaction(TransactionGateway transactionGateway) {

    public Transaction update(final Transaction transaction) {
        return transactionGateway.update(transaction);
    }
}
