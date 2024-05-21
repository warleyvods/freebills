package com.freebills.usecases.strategy.account;

import com.freebills.domain.Event;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.EventType.ACCOUNT_CREATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_CREATED;
import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;
import static java.lang.Boolean.TRUE;

@Component(value = "ACCOUNT_CREATED")
public class AccountCreatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(final BigDecimal currentBalance, final Event event) {
        if (event.getEventType() == ACCOUNT_CREATED) {
           return currentBalance.add(event.getTransactionData().getAmount());
        }

        return currentBalance;
    }
}
