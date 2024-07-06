package com.freebills.events;

import com.freebills.domain.Event;
import com.freebills.events.transfer.TransferCreatedEvent;
import com.freebills.events.transfer.TransferDeleteEvent;
import com.freebills.events.transfer.TransferUpdatedEvent;
import com.freebills.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static com.freebills.gateways.entities.enums.EventType.TRANSFER_CREATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_UPDATED;


@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleTransferCreatedEvent(final TransferCreatedEvent event) {
        log.info("Handling TransferCreatedEvent from account id: {} to account id: {}",
                event.getTransfer().getFrom().getId(),
                event.getTransfer().getTo().getId()
        );

        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setEventType(TRANSFER_CREATED);
            eventFromAccount.setAggregateId(event.getTransfer().getFrom().getId());
            eventFromAccount.setTransferData(event.getTransfer().withTransferTypeOut());

            final var eventToAccount = new Event();
            eventToAccount.setEventType(TRANSFER_CREATED);
            eventToAccount.setAggregateId(event.getTransfer().getTo().getId());
            eventToAccount.setTransferData(event.getTransfer().withTransferTypeIn());

            final var eventList = List.of(eventFromAccount, eventToAccount);
            eventGateway.saveAll(eventList);
        } catch (Exception e) {
            log.error("Error handling TransferCreatedEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleTransferDeleteEvent(final TransferDeleteEvent event) {
        log.info("Handling TransferDeleteEvent from account id: {} to account id: {}",
                event.getTransfer().getFrom().getId(),
                event.getTransfer().getTo().getId()
        );

        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setEventType(TRANSFER_DELETED);
            eventFromAccount.setAggregateId(event.getTransfer().getFrom().getId());
            eventFromAccount.setTransferData(event.getTransfer().withTransferTypeIn());

            final var eventToAccount = new Event();
            eventToAccount.setEventType(TRANSFER_DELETED);
            eventToAccount.setAggregateId(event.getTransfer().getTo().getId());
            eventToAccount.setTransferData(event.getTransfer().withTransferTypeOut());

            final var eventList = List.of(eventFromAccount, eventToAccount);

            eventList.forEach(eventGateway::save);

        } catch (Exception e) {
            log.error("Error handling TransferDeleteEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleTransferUpdateEvent(final TransferUpdatedEvent event) {
        log.info("Handling new TransferUpdatedEvent from account id: {} to account id: {}",
                event.getTransfer().getFrom().getId(),
                event.getTransfer().getTo().getId());

        log.info("Handling old TransferUpdatedEvent from account id: {} to account id: {}",
                event.getOldTransfer().getFrom().getId(),
                event.getOldTransfer().getTo().getId());

        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setEventType(TRANSFER_UPDATED);
            eventFromAccount.setAggregateId(event.getTransfer().getFrom().getId());

            final var eventToAccount = new Event();
            eventToAccount.setEventType(TRANSFER_UPDATED);
            eventToAccount.setAggregateId(event.getTransfer().getTo().getId());


            if (event.getOldTransfer().getFrom().getId().equals(event.getTransfer().getTo().getId())) {
                eventFromAccount.setTransferData(event.getTransfer().withTransferTypeOut());
                eventFromAccount.setOldTransferData(event.getOldTransfer().withTransferTypeOut());

                eventToAccount.setTransferData(event.getTransfer().withTransferTypeIn());
                eventToAccount.setOldTransferData(event.getTransfer().withTransferTypeIn());
            }

            eventFromAccount.setTransferData(event.getTransfer().withTransferTypeOut());
            eventFromAccount.setOldTransferData(event.getOldTransfer().withTransferTypeOut());

            eventToAccount.setTransferData(event.getTransfer().withTransferTypeIn());
            eventToAccount.setOldTransferData(event.getOldTransfer().withTransferTypeIn());

            final var eventList = List.of(eventFromAccount, eventToAccount);

            eventGateway.saveAll(eventList);

        } catch (Exception e) {
            log.error("Error handling TransferUpdateEvent: {}", e.getMessage());
            throw e;
        }
    }
}