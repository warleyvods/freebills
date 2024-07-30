package com.freebills.events.account;

import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.enums.AccountChangeType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountUpdateEvent extends ApplicationEvent {

    private final Long accountId;
    private final Transaction transaction;

    public AccountUpdateEvent(Object source, Long accountId, Transaction transaction) {
        super(source);
        this.accountId = accountId;
        this.transaction = transaction;
    }
}
