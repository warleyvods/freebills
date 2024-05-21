package com.freebills.events.transaction;

import com.freebills.domain.Transaction;
import com.freebills.events.BaseEvent;
import lombok.Getter;

@Getter
public class TransactionUpdatedEvent extends BaseEvent {

    public TransactionUpdatedEvent(Object source, Long accountId, Transaction transaction, Transaction oldTransaction) {
        super(source, accountId, transaction, oldTransaction);
    }
}
