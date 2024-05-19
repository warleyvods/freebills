package com.freebills.events.transaction;

import com.freebills.domain.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class BaseTransactionEvent extends ApplicationEvent {

    private final Long accountId;
    private final Transaction transaction;
    private final Transaction oldTransaction;

    protected BaseTransactionEvent(Object source,
                                   Long accountId,
                                   Transaction transaction,
                                   Transaction oldTransaction) {
        super(source);
        this.accountId = accountId;
        this.transaction = transaction;
        this.oldTransaction = oldTransaction;
    }

    protected BaseTransactionEvent(Object source, Long accountId, Transaction transaction) {
        this(source, accountId, transaction, null);
    }
}
