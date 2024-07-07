package com.freebills.events.transfer;

import com.freebills.domain.Transfer;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransferCreatedEvent extends ApplicationEvent {

    private final Transfer transfer;

    public TransferCreatedEvent(Object source, Transfer transfer) {
        super(source);
        this.transfer = transfer;
    }
}