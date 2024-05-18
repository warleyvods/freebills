package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.gateways.EventGateway;
import com.freebills.strategy.BalanceUpdateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator {

    private final EventGateway eventGateway;
    private final BalanceUpdateStrategyFactory strategyFactory;

    public BigDecimal calculateBalanceForAccount(Account account) {
        List<Event> events = eventGateway.getEventsByAggregateId(account.getId());

        BigDecimal balance = BigDecimal.ZERO;
        for (Event event : events) {
            BalanceUpdateStrategy strategy = strategyFactory.getStrategy(event.getEventType());
            balance = strategy.updateBalance(balance, event);
        }

        return balance;
    }
}