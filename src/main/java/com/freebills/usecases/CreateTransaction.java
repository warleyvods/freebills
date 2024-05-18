package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.mapper.TransactionGatewayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionValidation transactionValidation;
    private final TransactionGatewayMapper transactionGatewayMapper;

    public Transaction execute(final Transaction transaction) {
        if (transaction.getPreviousAmount() == null) {
            transaction.setPreviousAmount(transaction.getAmount());
        }

        final var transactionEntitySaved = transactionGateway.save(transaction);

        log.info("[createTransaction:{}] Creating new transaction", transactionEntitySaved.getId());
        transactionValidation.transactionCreationValidation(transactionEntitySaved);
        return transactionEntitySaved;
    }
}
