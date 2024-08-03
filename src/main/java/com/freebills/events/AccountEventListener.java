package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.account.AccountCreatedEvent;
import com.freebills.events.account.AccountDeletedEvent;
import com.freebills.events.account.AccountUpdateEvent;
import com.freebills.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_CREATED;
import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_DELETED;
import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_UPDATED;


@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleUpdateAccount(final AccountUpdateEvent event) {
        log.info("Handling AccountUpdatedEvent by account_id: {} ", event.getAccountId());

        try {
            final var newEvent = new Event();
            newEvent.setAggregateId(event.getAccountId());
            newEvent.setEventType(ACCOUNT_UPDATED);
            newEvent.setTransactionData(event.getTransaction());

            eventGateway.save(newEvent);
        } catch (Exception e) {
            log.error("Error handling AccountUpdatedEvent: {}", e.getMessage());
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