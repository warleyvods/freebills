package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public record CreateTransaction(TransactionGateway transactionGateway, TransactionValidation transactionValidation) {

    public Transaction create(final Transaction transaction) {
        final Transaction transactionSaved = transactionGateway.save(transaction);
        log.info("[createTransaction:{}] Creating new transaction", transactionSaved.getId());
        transactionValidation.transactionValidation(transactionSaved);
        return transactionSaved;
    }
}
