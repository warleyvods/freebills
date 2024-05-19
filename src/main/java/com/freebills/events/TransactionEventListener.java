package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.account.AccountCreatedEvent;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.events.transaction.TransactionDeletedEvent;
import com.freebills.events.transaction.TransactionUpdatedEvent;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.entities.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Handling TransactionCreatedEvent for account id: {}", event.getAccountId());
        try {
            Event newEvent = new Event();
            newEvent.setAggregateId(event.getAccountId());
            newEvent.setEventType(EventType.TRANSACTION_CREATED);
            newEvent.setTransactionData(event.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionCreatedEvent", e);
            throw e;
        }
    }

    @EventListener
    public void handleTransactionUpdatedEvent(TransactionUpdatedEvent event) {
        log.info("Handling TransactionUpdatedEvent for account id: {}", event.getAccountId());
        try {
            Event newEvent = new Event();
            newEvent.setAggregateId(event.getAccountId());
            newEvent.setEventType(EventType.TRANSACTION_UPDATED);
            newEvent.setTransactionData(event.getTransaction());
            newEvent.setOldTransactionData(event.getOldTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionUpdatedEvent", e);
            throw e;
        }
    }

    @EventListener
    public void handleTransactionDeletedEvent(TransactionDeletedEvent event) {
        log.info("Handling TransactionDeletedEvent for account id: {}", event.getAccountId());
        try {
            Event newEvent = new Event();
            newEvent.setAggregateId(event.getAccountId());
            newEvent.setEventType(EventType.TRANSACTION_DELETED);
            newEvent.setTransactionData(event.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionDeletedEvent", e);
            throw e;
        }
    }

    @EventListener
    public void handleAccountCreatedEvent(AccountCreatedEvent event) {
        log.info("Handling AccountCreatedEvent for account id: {}", event.getAccountId());
        try {
            Event newEvent = new Event();
            newEvent.setAggregateId(event.getAccountId());
            newEvent.setEventType(EventType.TRANSACTION_CREATED);
            newEvent.setTransactionData(event.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionDeletedEvent", e);
            throw e;
        }
    }
}
