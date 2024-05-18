package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionCreatedStrategyTest {

    private TransactionCreatedStrategy transactionCreatedStrategy;

    @BeforeEach
    public void setUp() {
        transactionCreatedStrategy = new TransactionCreatedStrategy();
    }

    @Test
    void testUpdateBalanceWithRevenueTransaction() {
        final Transaction transaction = new Transaction();
        transaction.setPaid(true);

        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setTransactionData(transaction);

        event.setEventType(EventType.TRANSACTION_CREATED);
        event.getTransactionData().setTransactionType(TransactionType.REVENUE);
        event.getTransactionData().setAmount(BigDecimal.valueOf(200));

        BigDecimal updatedBalance = transactionCreatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1200), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithExpenseTransaction() {
        final Transaction transaction = new Transaction();
        transaction.setPaid(true);

        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setTransactionData(transaction);

        event.setEventType(EventType.TRANSACTION_CREATED);
        event.getTransactionData().setTransactionType(TransactionType.EXPENSE);
        event.getTransactionData().setAmount(BigDecimal.valueOf(200));

        BigDecimal updatedBalance = transactionCreatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(800), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithNonCreatedEvent() {
        final Transaction transaction = new Transaction();
        transaction.setPaid(true);

        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setTransactionData(transaction);

        event.setEventType(EventType.TRANSACTION_UPDATED);
        event.getTransactionData().setTransactionType(TransactionType.REVENUE);
        event.getTransactionData().setAmount(BigDecimal.valueOf(200));

        BigDecimal updatedBalance = transactionCreatedStrategy.updateBalance(currentBalance, event);

        assertEquals(currentBalance, updatedBalance);
    }
}
