package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateTransaction(TransactionGateway transactionGateway, TransactionValidation transactionValidation) {

    public Transaction update(final Transaction transaction) {
        if (Boolean.FALSE.equals(transaction.getBankSlip())) {
            transaction.setBarCode(null);
        }

        final Transaction transactionSaved = transactionGateway.update(transaction);
        transactionValidation.transactionUpdateValidation(transactionSaved);
        return transactionSaved;
    }
}
