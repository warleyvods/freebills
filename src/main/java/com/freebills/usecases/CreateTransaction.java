package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.gateways.TransactionGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTransaction {

    private final TransactionGateway transactionGateway;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Transaction execute(final Transaction transaction) {
        final var savedTransaction = transactionGateway.save(transaction);
        log.info("Transaction created with id: {}", savedTransaction.getId());

        eventPublisher.publishEvent(new TransactionCreatedEvent(this, savedTransaction.getAccount().getId(), savedTransaction));
        return savedTransaction;
    }
}
