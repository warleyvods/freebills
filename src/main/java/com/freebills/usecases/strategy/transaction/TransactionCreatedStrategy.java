package com.freebills.usecases.strategy.transaction;

import com.freebills.domain.Event;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_CREATED;
import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;
import static java.lang.Boolean.TRUE;

@Component(value = "TRANSACTION_CREATED")
public class TransactionCreatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(final BigDecimal currentBalance, final Event event) {
        if (event.getEventType() == TRANSACTION_CREATED && TRUE.equals(event.getTransactionData().getPaid())) {
            if (event.getTransactionData().getTransactionType() == EXPENSE) {
                return currentBalance.subtract(event.getTransactionData().getAmount());
            } else if (event.getTransactionData().getTransactionType() == REVENUE) {
                return currentBalance.add(event.getTransactionData().getAmount());
            }
        }

        return currentBalance;
    }
}
