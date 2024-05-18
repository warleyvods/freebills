package com.freebills.strategy;

import com.freebills.domain.Event;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionDeletedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        return currentBalance.subtract(event.getTransactionAmount());
    }
}