package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator {

    private final EventGateway eventGateway;
    private final BalanceUpdateFactory strategyFactory;

    @Cacheable(value = "account", key = "#account.id")
    public BigDecimal calculateBalanceForAccount(final Account account) {

        var events = eventGateway.getEventsByAggregateId(account.getId());
        var strategyCache = new ConcurrentHashMap<EventType, BalanceUpdateStrategy>();

        return events.parallelStream()
                .collect(Collectors.groupingBy(Event::getEventType))
                .entrySet().parallelStream()
                .map(entry -> {
                    strategyCache.computeIfAbsent(entry.getKey(), strategyFactory::getStrategy);
                    BalanceUpdateStrategy strategy = strategyCache.get(entry.getKey());
                    return entry.getValue().stream()
                            .reduce(ZERO, strategy::updateBalance, BigDecimal::add);
                })
                .reduce(ZERO, BigDecimal::add);
    }
}
