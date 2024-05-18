package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.TransactionType.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionUpdatedStrategyTest {

    private TransactionUpdatedStrategy transactionUpdatedStrategy;

    @BeforeEach
    public void setUp() {
        transactionUpdatedStrategy = new TransactionUpdatedStrategy();
    }

    @Test
    void testUpdateBalanceWithExpenseTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setEventType(EventType.TRANSACTION_UPDATED);
        event.setOldTransactionAmount(BigDecimal.valueOf(100));
        event.setNewTransactionAmount(BigDecimal.valueOf(200));

        event.setOldTransactionType(EXPENSE);
        event.setNewTransactionType(EXPENSE);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithBalanceZero() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setEventType(EventType.TRANSACTION_UPDATED);
        event.setOldTransactionAmount(BigDecimal.valueOf(100));
        event.setNewTransactionAmount(BigDecimal.valueOf(100));

        event.setOldTransactionType(EXPENSE);
        event.setNewTransactionType(REVENUE);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(900), updatedBalance);
    }
}