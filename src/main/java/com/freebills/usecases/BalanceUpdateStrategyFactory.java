package com.freebills.usecases;

import com.freebills.gateways.entities.enums.EventType;
import com.freebills.strategy.BalanceUpdateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BalanceUpdateStrategyFactory {

    private final Map<String, BalanceUpdateStrategy> strategyMap;

    public BalanceUpdateStrategy getStrategy(EventType eventType) {
        BalanceUpdateStrategy strategy = strategyMap.get(eventType.name());
        if (strategy == null) {
            throw new IllegalArgumentException("invalid type: " + eventType);
        }
        return strategy;
    }
}