package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.account.AccountCreatedEvent;
import com.freebills.events.account.AccountDeletedEvent;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.events.transaction.TransactionDeletedEvent;
import com.freebills.events.transaction.TransactionUpdatedEvent;
import com.freebills.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_CREATED;
import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_CREATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_TRANSFER;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_UPDATED;

@Slf4j
@Component
@RequiredArgsConstructor
public class BaseEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleTransactionCreatedEvent(final TransactionCreatedEvent transactionCreatedEvent) {
        log.info("Handling TransactionCreatedEvent for account id: {}", transactionCreatedEvent.getAccountId());
        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(transactionCreatedEvent.getAccountId());
            newEvent.setEventType(TRANSACTION_CREATED);
            newEvent.setTransactionData(transactionCreatedEvent.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionCreatedEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleTransactionUpdatedEvent(final TransactionUpdatedEvent transactionUpdatedEvent) {
        log.info("Handling TransactionUpdatedEvent for account id: {}", transactionUpdatedEvent.getAccountId());
        try {
            if (!transactionUpdatedEvent.getTransaction().getAccount().getId().equals(transactionUpdatedEvent.getOldTransaction().getAccount().getId())) {
                final var eventTransfer = new Event();
                eventTransfer.setAggregateId(transactionUpdatedEvent.getOldTransaction().getAccount().getId());
                eventTransfer.setEventType(TRANSACTION_TRANSFER);
                eventTransfer.setTransactionData(transactionUpdatedEvent.getTransaction());
                eventTransfer.setOldTransactionData(transactionUpdatedEvent.getOldTransaction());

                final var createEvent = new Event();
                createEvent.setAggregateId(transactionUpdatedEvent.getTransaction().getAccount().getId());
                createEvent.setEventType(TRANSACTION_CREATED);
                createEvent.setTransactionData(transactionUpdatedEvent.getTransaction());

                eventGateway.saveAll(List.of(eventTransfer, createEvent));
                return;
            }

            final var newEvent = new Event();
            newEvent.setAggregateId(transactionUpdatedEvent.getAccountId());
            newEvent.setEventType(TRANSACTION_UPDATED);
            newEvent.setTransactionData(transactionUpdatedEvent.getTransaction());
            newEvent.setOldTransactionData(transactionUpdatedEvent.getOldTransaction());

            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionUpdatedEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleTransactionDeletedEvent(final TransactionDeletedEvent transactionDeletedEvent) {
        log.info("Handling TransactionDeletedEvent for account id: {}", transactionDeletedEvent.getAccountId());
        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(transactionDeletedEvent.getAccountId());
            newEvent.setEventType(TRANSACTION_DELETED);
            newEvent.setTransactionData(transactionDeletedEvent.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling TransactionDeletedEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleAccountCreatedEvent(final AccountCreatedEvent accountCreatedEvent) {
        log.info("Handling AccountCreatedEvent for account id: {}", accountCreatedEvent.getAccountId());
        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(accountCreatedEvent.getAccountId());
            newEvent.setEventType(ACCOUNT_CREATED);
            newEvent.setTransactionData(accountCreatedEvent.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling AccountCreatedEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleAccountDeletedEvent(final AccountDeletedEvent accountDeletedEvent) {
        log.info("Handling AccountDeletedEvent for account id: {}", accountDeletedEvent.getAccountId());
        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(accountDeletedEvent.getAccountId());
            newEvent.setEventType(ACCOUNT_DELETED);
            newEvent.setTransactionData(accountDeletedEvent.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling AccountDeletedEvent: {}", e.getMessage());
            throw e;
        }
    }
}
