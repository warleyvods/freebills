package com.freebills.events.transfer;

import com.freebills.domain.Transfer;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransferUpdatedEvent extends ApplicationEvent {

    private final Transfer transfer;
    private final Transfer oldTransfer;

    public TransferUpdatedEvent(Object source, Transfer transfer, Transfer oldTransfer) {
        super(source);
        this.transfer = transfer;
        this.oldTransfer = oldTransfer;
    }

}
