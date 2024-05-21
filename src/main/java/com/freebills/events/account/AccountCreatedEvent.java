package com.freebills.events.account;

import com.freebills.domain.Transaction;
import com.freebills.events.BaseEvent;
import lombok.Getter;

@Getter
public class AccountCreatedEvent extends BaseEvent {

    public AccountCreatedEvent(Object source, Long accountId, Transaction transaction) {
        super(source, accountId, transaction);
    }
}