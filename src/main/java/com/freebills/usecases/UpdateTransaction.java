package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.events.transaction.TransactionUpdatedEvent;
import com.freebills.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTransaction {

    private final TransactionGateway transactionGateway;
    private final ApplicationEventPublisher eventPublisher;

    public Transaction execute(final Transaction transaction) {
        Transaction oldTransaction = transactionGateway.findById(transaction.getId());

        final var savedTransaction = transactionGateway.update(transaction);

        eventPublisher.publishEvent(new TransactionUpdatedEvent(
                this,
                savedTransaction.getAccount().getId(),
                savedTransaction.getAmount(),
                oldTransaction.getAmount(),
                savedTransaction.getAmount(),
                savedTransaction.getTransactionType(),
                oldTransaction.getTransactionType(),
                savedTransaction.getTransactionType()
        ));
        return savedTransaction;
    }
}
