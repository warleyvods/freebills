package com.freebills.events;

import com.freebills.domain.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class BaseEvent extends ApplicationEvent {

    private final Long accountId;
    private final Transaction transaction;
    private final Transaction oldTransaction;

    protected BaseEvent(final Object source,
                        final Long accountId,
                        final Transaction transaction,
                        final Transaction oldTransaction) {
        super(source);
        this.accountId = accountId;
        this.transaction = transaction;
        this.oldTransaction = oldTransaction;
    }

    protected BaseEvent(Object source, Long accountId, Transaction transaction) {
        this(source, accountId, transaction, null);
    }
}
