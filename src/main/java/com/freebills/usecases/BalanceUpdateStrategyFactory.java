package com.freebills.usecases;

import com.freebills.gateways.entities.enums.EventType;
import com.freebills.strategy.BalanceUpdateStrategy;
import com.freebills.strategy.TransactionCreatedStrategy;
import com.freebills.strategy.TransactionDeletedStrategy;
import com.freebills.strategy.TransactionUpdatedStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceUpdateStrategyFactory {

    private final TransactionCreatedStrategy transactionCreatedStrategy;
    private final TransactionUpdatedStrategy transactionUpdatedStrategy;
    private final TransactionDeletedStrategy transactionDeletedStrategy;

    public BalanceUpdateStrategy getStrategy(EventType eventType) {
        return switch (eventType) {
            case TRANSACTION_CREATED -> transactionCreatedStrategy;
            case TRANSACTION_UPDATED -> transactionUpdatedStrategy;
            case TRANSACTION_DELETED -> transactionDeletedStrategy;
        };
    }
}