package com.freebills.events.transaction;

import com.freebills.domain.Transaction;
import com.freebills.events.BaseEvent;
import lombok.Getter;

@Getter
public class TransactionDeletedEvent extends BaseEvent {

    public TransactionDeletedEvent(Object source, Long accountId, Transaction transaction) {
        super(source, accountId, transaction);
    }
}