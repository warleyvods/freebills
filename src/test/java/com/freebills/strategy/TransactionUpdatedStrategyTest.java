package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
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
        final Transaction transaction = new Transaction();
        transaction.setPaid(true);

        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = new Event();
        event.setTransactionData(transaction);
        event.setOldTransactionData(transaction);

        event.setEventType(EventType.TRANSACTION_UPDATED);
        event.getOldTransactionData().setAmount(BigDecimal.valueOf(100));
        event.getTransactionData().setAmount(BigDecimal.valueOf(200));

        event.getOldTransactionData().setTransactionType(EXPENSE);
        event.getTransactionData().setTransactionType(EXPENSE);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithBalanceZero() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        final Transaction transactionNew = new Transaction();
        transactionNew.setPaid(true);
        transactionNew.setAmount(BigDecimal.valueOf(100));
        transactionNew.setTransactionType(EXPENSE);

        final Transaction transactionOld = new Transaction();
        transactionOld.setPaid(true);
        transactionOld.setAmount(BigDecimal.valueOf(100));
        transactionOld.setTransactionType(REVENUE);

        Event event = new Event();
        event.setTransactionData(transactionNew);
        event.setOldTransactionData(transactionOld);
        event.setEventType(EventType.TRANSACTION_UPDATED);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(900), updatedBalance);
    }
}