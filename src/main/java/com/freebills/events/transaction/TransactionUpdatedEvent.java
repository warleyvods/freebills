package com.freebills.events.transaction;

import com.freebills.domain.Transaction;
import lombok.Getter;

@Getter
public class TransactionUpdatedEvent extends BaseTransactionEvent {

    public TransactionUpdatedEvent(Object source, Long accountId, Transaction transaction, Transaction oldTransaction) {
        super(source, accountId, transaction, oldTransaction);
    }
}
