package com.freebills.events.transaction;

import com.freebills.events.BaseTransactionEvent;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionDeletedEvent extends BaseTransactionEvent {

    public TransactionDeletedEvent(Object source, Long accountId, BigDecimal transactionAmount, TransactionType transactionType) {
        super(source, accountId, transactionAmount, transactionType);
    }
}