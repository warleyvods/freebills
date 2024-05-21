package com.freebills.events.account;

import com.freebills.domain.Transaction;
import com.freebills.events.BaseEvent;
import lombok.Getter;

@Getter
public class AccountDeletedEvent extends BaseEvent {

    public AccountDeletedEvent(Object source, Long accountId, Transaction transaction) {
        super(source, accountId, transaction);
    }
}
