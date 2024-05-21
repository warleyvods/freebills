package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.gateways.EventGateway;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator {

    private final EventGateway eventGateway;
    private final BalanceUpdateFactory strategyFactory;

    public BigDecimal calculateBalanceForAccount(final Account account) {
        var events = eventGateway.getEventsByAggregateId(account.getId());

        var balance = ZERO;
        for (Event event : events) {
            BalanceUpdateStrategy strategy = strategyFactory.getStrategy(event.getEventType());
            balance = strategy.updateBalance(balance, event);
        }

        return balance;
    }
}