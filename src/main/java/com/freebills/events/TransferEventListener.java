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

import static com.freebills.gateways.entities.enums.EventType.TRANSFER_CREATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_UPDATED;
import static java.util.List.of;


@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventListener {

    private final EventGateway eventGateway;

    @EventListener
    public void handleTransferCreatedEvent(final TransferCreatedEvent event) {
        log.info("Handling TransferCreatedEvent from account id: {} to account id: {}",
                event.getTransfer().getFromAccountId().getId(),
                event.getTransfer().getToAccountId().getId()
        );

        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setEventType(TRANSFER_CREATED);
            eventFromAccount.setAggregateId(event.getTransfer().getFromAccountId().getId());
            eventFromAccount.setTransferData(event.getTransfer().withTransferTypeOut());

            final var eventToAccount = new Event();
            eventToAccount.setEventType(TRANSFER_CREATED);
            eventToAccount.setAggregateId(event.getTransfer().getToAccountId().getId());
            eventToAccount.setTransferData(event.getTransfer().withTransferTypeIn());

            eventGateway.saveAll(of(eventFromAccount, eventToAccount));
        } catch (Exception e) {
            log.error("Error handling TransferCreatedEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleTransferDeleteEvent(final TransferDeleteEvent event) {
        log.info("Handling TransferDeleteEvent from account id: {} to account id: {}",
                event.getTransfer().getFromAccountId().getId(),
                event.getTransfer().getToAccountId().getId()
        );

        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setEventType(TRANSFER_DELETED);
            eventFromAccount.setAggregateId(event.getTransfer().getFromAccountId().getId());
            eventFromAccount.setTransferData(event.getTransfer().withTransferTypeIn());

            final var eventToAccount = new Event();
            eventToAccount.setEventType(TRANSFER_DELETED);
            eventToAccount.setAggregateId(event.getTransfer().getToAccountId().getId());
            eventToAccount.setTransferData(event.getTransfer().withTransferTypeOut());

            eventGateway.saveAll(of(eventFromAccount, eventToAccount));
        } catch (Exception e) {
            log.error("Error handling TransferDeleteEvent: {}", e.getMessage());
            throw e;
        }
    }

    @EventListener
    public void handleTransferUpdateEvent(final TransferUpdatedEvent event) {
        log.info("Handling new TransferUpdatedEvent from account id: {} to account id: {}",
                event.getTransfer().getFromAccountId().getId(),
                event.getTransfer().getToAccountId().getId());

        log.info("Handling old TransferUpdatedEvent from account id: {} to account id: {}",
                event.getOldTransfer().getFromAccountId().getId(),
                event.getOldTransfer().getToAccountId().getId());

        try {
            final var eventFromAccount = new Event();
            eventFromAccount.setEventType(TRANSFER_UPDATED);
            eventFromAccount.setAggregateId(event.getTransfer().getFromAccountId().getId());

            final var eventToAccount = new Event();
            eventToAccount.setEventType(TRANSFER_UPDATED);
            eventToAccount.setAggregateId(event.getTransfer().getToAccountId().getId());

            eventFromAccount.setTransferData(event.getTransfer().withTransferTypeOut());
            eventFromAccount.setOldTransferData(event.getOldTransfer().withTransferTypeOut());

            eventToAccount.setTransferData(event.getTransfer().withTransferTypeIn());
            eventToAccount.setOldTransferData(event.getOldTransfer().withTransferTypeIn());

            if (event.getOldTransfer().getFromAccountId().getId().equals(event.getTransfer().getToAccountId().getId()) &&
                event.getOldTransfer().getToAccountId().getId().equals(event.getTransfer().getFromAccountId().getId())) {

                eventFromAccount.setOldTransferData(event.getOldTransfer().withTransferTypeIn());
                eventToAccount.setOldTransferData(event.getOldTransfer().withTransferTypeOut());
            }

            eventGateway.saveAll(of(eventFromAccount, eventToAccount));
        } catch (Exception e) {
            log.error("Error handling TransferUpdateEvent: {}", e.getMessage());
            throw e;
        }
    }
}