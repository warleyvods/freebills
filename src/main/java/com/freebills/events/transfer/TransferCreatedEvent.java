package com.freebills.events.transfer;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public class TransferCreatedEvent extends ApplicationEvent {

    private final Long fromAccountId;
    private final Long toAccountId;
    private final BigDecimal amount;

    public TransferCreatedEvent(Object source, Long fromAccountId, Long toAccountId, BigDecimal amount) {
        super(source);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }
}