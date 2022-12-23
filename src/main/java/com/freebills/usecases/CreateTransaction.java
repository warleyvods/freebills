package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTransaction {

    private final TransactionGateway transactionGateway;
    private final  TransactionValidation transactionValidation;

    public Transaction execute(final Transaction transaction) {
        final Transaction transactionSaved = transactionGateway.save(transaction);
        log.info("[createTransaction:{}] Creating new transaction", transactionSaved.getId());
        transactionValidation.transactionCreationValidation(transactionSaved);
        return transactionSaved;
    }
}
