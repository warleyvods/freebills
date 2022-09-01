package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateTransaction(TransactionGateway transactionGateway, TransactionValidation transactionValidation) {

    public Transaction update(final Transaction transaction) {
        final Transaction transactionSaved = transactionGateway.update(transaction);
        transactionValidation.transactionValidation(transactionSaved);
        return transactionSaved;
    }
}
