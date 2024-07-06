package com.freebills.usecases;

import com.freebills.domain.Transfer;
import com.freebills.events.transfer.TransferDeleteEvent;
import com.freebills.gateways.TransferGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTransfer {

    private final TransferGateway transferGateway;
    private final ApplicationEventPublisher eventPublisher;
    private final FindTransfer findTransfer;

    public void execute(final Long id, final String username) {
        final Transfer transferFounded = findTransfer.byId(id, username);

        transferGateway.deleteById(id, username);

        eventPublisher.publishEvent(new TransferDeleteEvent(this, transferFounded));
    }
}
