package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CreateTransaction {

    private final TransactionGateway transactionGateway;
    private final ApplicationEventPublisher eventPublisher;

    public Transaction execute(final Transaction transaction) {
        final var savedTransaction = transactionGateway.save(transaction);

        eventPublisher.publishEvent(new TransactionCreatedEvent(this, savedTransaction.getAccount().getId(), savedTransaction));
        return savedTransaction;
    }
}
