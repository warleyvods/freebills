package com.freebills.usecases;

import com.freebills.domain.Transfer;
import com.freebills.events.transfer.TransferUpdatedEvent;
import com.freebills.gateways.TransferGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTransfer {

    private final TransferGateway transferGateway;
    private final ApplicationEventPublisher eventPublisher;

    public Transfer execute(final Transfer transfer, final String username) {
        if (transfer.getFromAccountId().equals(transfer.getToAccountId())) {
            throw new IllegalArgumentException("A conta de origem e destino não podem ser a mesma.");
        }

        final var oldTransfer = transferGateway.findById(transfer.getId(), username);
        final var newTransfer = transferGateway.update(transfer);

        eventPublisher.publishEvent(new TransferUpdatedEvent(this, newTransfer, oldTransfer));
        return newTransfer;
    }
}
