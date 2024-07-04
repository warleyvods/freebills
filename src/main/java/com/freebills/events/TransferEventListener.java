package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.transfer.TransferCreatedEvent;
import com.freebills.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.freebills.gateways.entities.enums.EventType.TRANSFER;
import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;


@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleTransferCreatedEvent(final TransferCreatedEvent event) {
        log.info("Handling TransferCreatedEvent from account id: {} to account id: {}", event.getFromAccountId(), event.getFromAccountId());
        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setAggregateId(event.getFromAccountId());
            eventFromAccount.setEventType(TRANSFER);
            eventFromAccount.setTransferType(OUT);
            eventFromAccount.setAmount(event.getAmount());

            final var eventToAccount = new Event();
            eventToAccount.setAggregateId(event.getToAccountId());
            eventToAccount.setEventType(TRANSFER);
            eventToAccount.setTransferType(IN);
            eventToAccount.setAmount(event.getAmount());

            final var eventList = List.of(eventFromAccount, eventToAccount);
            eventList.forEach(eventGateway::save);
        } catch (Exception e) {
            log.error("Error handling TransferCreatedEvent: {}", e.getMessage());
            throw e;
        }
    }
}