package com.freebills.usecases;

import com.freebills.events.transaction.TransactionDeletedEvent;
import com.freebills.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTransaction {

    private final TransactionGateway transactionGateway;
    private final ApplicationEventPublisher eventPublisher;

    public void delete(final Long id)  {
        final var transaction = transactionGateway.findById(id);

        transactionGateway.delete(id);

        eventPublisher.publishEvent(new TransactionDeletedEvent(this, transaction.getAccount().getId(), transaction));
    }
}
