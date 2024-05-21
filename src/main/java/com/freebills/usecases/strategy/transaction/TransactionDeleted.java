package com.freebills.usecases.strategy.transaction;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.lang.Boolean.TRUE;

@Component(value = "TRANSACTION_DELETED")
public class TransactionDeleted implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(final BigDecimal currentBalance, final Event event) {
        if (event.getEventType() == EventType.TRANSACTION_DELETED && TRUE.equals(event.getTransactionData().getPaid())) {
            if (event.getTransactionData().getTransactionType() == TransactionType.EXPENSE) {
                return currentBalance.add(event.getTransactionData().getAmount());
            } else if (event.getTransactionData().getTransactionType() == TransactionType.REVENUE) {
                return currentBalance.subtract(event.getTransactionData().getAmount());
            }
        }

        return currentBalance;
    }
}