package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.account.AccountCreatedEvent;
import com.freebills.events.account.AccountDeletedEvent;
import com.freebills.events.account.AccountrReajustEvent;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.events.transaction.TransactionDeletedEvent;
import com.freebills.events.transaction.TransactionUpdatedEvent;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.entities.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_CREATED;
import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_DELETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleTransactionCreatedEvent(final TransactionCreatedEvent transactionCreatedEvent) {
        log.info("Handling TransactionCreatedEvent for account id: {}", transactionCreatedEvent.getAccountId());
        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(transactionCreatedEvent.getAccountId());
            newEvent.setEventType(EventType.TRANSACTION_CREATED);
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
            final var newEvent = new Event();
            newEvent.setAggregateId(transactionUpdatedEvent.getAccountId());
            newEvent.setEventType(EventType.TRANSACTION_UPDATED);
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

    @EventListener
    public void handleAccountReajustEvent(final AccountrReajustEvent accountrReajustEvent) {
        log.info("Handling AccountrReajustEvent for account id: {}", accountrReajustEvent.getAccountId());
        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(accountrReajustEvent.getAccountId());
            newEvent.setEventType(ACCOUNT_DELETED);
            newEvent.setTransactionData(accountrReajustEvent.getTransaction());
            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling AccountrReajustEvent: {}", e.getMessage());
            throw e;
        }
    }
}
