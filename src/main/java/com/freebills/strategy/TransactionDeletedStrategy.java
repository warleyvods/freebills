package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.lang.Boolean.TRUE;

@Component
public class TransactionDeletedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        if (event.getEventType() == EventType.TRANSACTION_DELETED && TRUE.equals(event.getPaid())) {
            if (event.getTransactionType() == TransactionType.EXPENSE) {
                return currentBalance.add(event.getTransactionAmount());
            } else if (event.getTransactionType() == TransactionType.REVENUE) {
                return currentBalance.subtract(event.getTransactionAmount());
            }
        }

        return currentBalance;
    }
}