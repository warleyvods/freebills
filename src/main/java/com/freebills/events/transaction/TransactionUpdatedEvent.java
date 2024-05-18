package com.freebills.events.transaction;

import com.freebills.events.BaseTransactionEvent;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionUpdatedEvent extends BaseTransactionEvent {

    public TransactionUpdatedEvent(Object source,
                                   Long accountId,
                                   BigDecimal transactionAmount,
                                   BigDecimal oldTransactionAmount,
                                   BigDecimal newTransactionAmount,
                                   TransactionType transactionType,
                                   TransactionType oldTransactionType,
                                   TransactionType newTransactionType) {
        super(source, accountId, transactionAmount, oldTransactionAmount, newTransactionAmount, transactionType, oldTransactionType, newTransactionType);
    }
}