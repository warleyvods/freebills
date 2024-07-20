package com.freebills.usecases.strategy.account;

import com.freebills.domain.Event;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_UPDATED;
import static java.math.BigDecimal.ZERO;

@Component(value = "ACCOUNT_UPDATED")
public class AccountUpdated implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(final BigDecimal currentBalance, final Event event) {

        if (event.getEventType() == ACCOUNT_UPDATED ) {
            BigDecimal transactionAmount = event.getTransactionData().getAmount();
            return transactionAmount.compareTo(ZERO) == 0 ? ZERO : transactionAmount;
        }

        return currentBalance;
    }
}
