package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.account.AccountUpdateEvent;
import com.freebills.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_UPDATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_CREATED;


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
}