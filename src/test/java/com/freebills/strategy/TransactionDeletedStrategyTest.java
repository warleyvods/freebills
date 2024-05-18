package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDeletedStrategyTest {

    private TransactionDeletedStrategy transactionCreatedStrategy;

    @BeforeEach
    public void setUp() {
        transactionCreatedStrategy = new TransactionDeletedStrategy();
    }

    @Test
    void testUpdateBalanceWithRevenueTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setEventType(EventType.TRANSACTION_DELETED);
        event.setTransactionType(TransactionType.REVENUE);
        event.setTransactionAmount(BigDecimal.valueOf(200));

        BigDecimal updatedBalance = transactionCreatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(800), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithExpenseTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setEventType(EventType.TRANSACTION_DELETED);
        event.setTransactionType(TransactionType.EXPENSE);
        event.setTransactionAmount(BigDecimal.valueOf(200));

        BigDecimal updatedBalance = transactionCreatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1200), updatedBalance);
    }
}
