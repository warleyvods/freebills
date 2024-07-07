package com.freebills.usecases;

import com.freebills.domain.Transfer;
import com.freebills.events.transfer.TransferCreatedEvent;
import com.freebills.gateways.TransferGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTransfer {

    private final TransferGateway transferGateway;
    private final ApplicationEventPublisher eventPublisher;

    public Transfer execute(Transfer transfer) {
        final Transfer transferSaved = transferGateway.save(transfer);

        eventPublisher.publishEvent(new TransferCreatedEvent(this, transferSaved));
        return transferSaved;
    }
}
