package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionCreatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        if (event.getEventType() == EventType.TRANSACTION_CREATED) {
            if (event.getTransactionType() == TransactionType.EXPENSE) {
                return currentBalance.subtract(event.getTransactionAmount());
            } else if (event.getTransactionType() == TransactionType.REVENUE) {
                return currentBalance.add(event.getTransactionAmount());
            }
        }
        return currentBalance;
    }
}