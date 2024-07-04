package com.freebills.usecases;

import com.freebills.events.transfer.TransferCreatedEvent;
import com.freebills.gateways.AccountGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransferBalance {

    private final ApplicationEventPublisher eventPublisher;
    private final AccountGateway accountGateway;


    public void excecute(final BigDecimal amount, final Long fromAccountId, final Long toAccountId) {
        final var fromAccount = accountGateway.findById(fromAccountId);
        final var toAccount = accountGateway.findById(toAccountId);

        final var transferCreatedEvent = new TransferCreatedEvent(this, fromAccount.getId(), toAccount.getId(), amount);

    }
}
