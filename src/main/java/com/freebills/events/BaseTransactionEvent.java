package com.freebills.events;

import com.freebills.gateways.entities.enums.TransactionType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public abstract class BaseTransactionEvent extends ApplicationEvent {

    private final Long accountId;
    private final BigDecimal transactionAmount;
    private final BigDecimal oldTransactionAmount;
    private final BigDecimal newTransactionAmount;
    private final TransactionType transactionType;
    private final TransactionType oldTransactionType;
    private final TransactionType newTransactionType;

    protected BaseTransactionEvent(Object source,
                                   Long accountId,
                                   BigDecimal transactionAmount,
                                   BigDecimal oldTransactionAmount,
                                   BigDecimal newTransactionAmount,
                                   TransactionType transactionType,
                                   TransactionType oldTransactionType,
                                   TransactionType newTransactionType) {
        super(source);
        this.accountId = accountId;
        this.transactionAmount = transactionAmount;
        this.oldTransactionAmount = oldTransactionAmount;
        this.newTransactionAmount = newTransactionAmount;
        this.transactionType = transactionType;
        this.oldTransactionType = oldTransactionType;
        this.newTransactionType = newTransactionType;
    }

    protected BaseTransactionEvent(Object source, Long accountId, BigDecimal transactionAmount, TransactionType transactionType) {
        this(source, accountId, transactionAmount, null, null, transactionType, null, null);
    }
}