package com.freebills.events.transaction;

import com.freebills.domain.Transaction;
import lombok.Getter;

@Getter
public class TransactionCreatedEvent extends BaseTransactionEvent {

    public TransactionCreatedEvent(Object source, Long accountId, Transaction transaction) {
        super(source, accountId, transaction);
    }
}