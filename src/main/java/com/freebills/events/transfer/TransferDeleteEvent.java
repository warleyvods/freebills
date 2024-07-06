package com.freebills.events.transfer;

import com.freebills.domain.Transfer;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransferDeleteEvent extends ApplicationEvent {

    private final Transfer transfer;

    public TransferDeleteEvent(Object source, Transfer transfer) {
        super(source);
        this.transfer = transfer;
    }
}
