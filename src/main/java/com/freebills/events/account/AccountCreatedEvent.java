package com.freebills.events.account;

import com.freebills.domain.Transaction;
import com.freebills.events.BaseTransactionEvent;
import lombok.Getter;

@Getter
public class AccountCreatedEvent extends BaseTransactionEvent {

    public AccountCreatedEvent(Object source, Long accountId, Transaction transaction) {
        super(source, accountId, transaction);
    }
}