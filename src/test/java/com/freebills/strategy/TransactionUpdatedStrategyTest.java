package com.freebills.strategy;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.usecases.strategy.TransactionUpdatedStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionUpdatedStrategyTest {

    private TransactionUpdatedStrategy transactionUpdatedStrategy;

    @BeforeEach
    public void setUp() {
        transactionUpdatedStrategy = new TransactionUpdatedStrategy();
    }

    @Test
    void testUpdateBalanceWithExpenseTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.EXPENSE, true, BigDecimal.valueOf(100),
                TransactionType.EXPENSE, true, BigDecimal.valueOf(200), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(900), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithBalanceZero() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(
                TransactionType.REVENUE,
                true,
                BigDecimal.valueOf(100),
                TransactionType.EXPENSE,
                true,
                BigDecimal.valueOf(100),
                1L,
                1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(800), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithRevenueTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(200), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithExpenseToRevenue() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.EXPENSE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(100), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1200), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithUnpaidTransactionUpdated() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, false, BigDecimal.valueOf(100),
                TransactionType.REVENUE, false, BigDecimal.valueOf(200), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1000), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithSameAccount() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.EXPENSE, true, BigDecimal.valueOf(100), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(800), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithUnpaidToPaid() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, false, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(100), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithPaidToUnpaid() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.EXPENSE, true, BigDecimal.valueOf(100),
                TransactionType.EXPENSE, false, BigDecimal.valueOf(100), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithTransactionZeroAmount() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(0), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(900), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithNegativeBalance() {
        BigDecimal currentBalance = BigDecimal.valueOf(-500);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(200), 1L, 1L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(-400), updatedBalance);
    }



    @Test
    void testUpdateBalanceWithPastRevenueTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEventWithDate(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(200), 1L, 1L, LocalDate.now().minusDays(1));

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithPastExpenseTransaction() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEventWithDate(TransactionType.EXPENSE, true, BigDecimal.valueOf(100),
                TransactionType.EXPENSE, true, BigDecimal.valueOf(200), 1L, 1L, LocalDate.now().minusDays(1));

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(900), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithAccountChangeUnpaid() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, false, BigDecimal.valueOf(100), 1L, 2L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(900), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithAccountChangeToPaid() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, false, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(100), 1L, 2L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1100), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithRevenueToOtherAccountWithNegativeBalance() {
        BigDecimal currentBalance = BigDecimal.valueOf(-500);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(200), 1L, 2L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(-400), updatedBalance);
    }



    @Test
    void testUpdateBalanceWithRevenueToOtherAccountPartiallyPaid() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.REVENUE, true, BigDecimal.valueOf(100),
                TransactionType.REVENUE, true, BigDecimal.valueOf(50), 1L, 2L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(950), updatedBalance);
    }

    @Test
    void testUpdateBalanceWithExpenseToOtherAccountPartiallyPaid() {
        BigDecimal currentBalance = BigDecimal.valueOf(1000);

        Event event = createEvent(TransactionType.EXPENSE, true, BigDecimal.valueOf(100),
                TransactionType.EXPENSE, true, BigDecimal.valueOf(50), 1L, 2L);

        BigDecimal updatedBalance = transactionUpdatedStrategy.updateBalance(currentBalance, event);

        assertEquals(BigDecimal.valueOf(1050), updatedBalance);
    }

    // Métodos auxiliares para criar eventos de teste
    private Event createEvent(TransactionType oldType, boolean oldPaid, BigDecimal oldAmount,
                              TransactionType newType, boolean newPaid, BigDecimal newAmount, Long oldAccountId, Long newAccountId) {
        Event event = new Event();

        Transaction oldTransaction = mock(Transaction.class);
        when(oldTransaction.getTransactionType()).thenReturn(oldType);
        when(oldTransaction.getPaid()).thenReturn(oldPaid);
        when(oldTransaction.getAmount()).thenReturn(oldAmount);
        Account oldAccount = mock(Account.class);
        when(oldAccount.getId()).thenReturn(oldAccountId);
        when(oldTransaction.getAccount()).thenReturn(oldAccount);

        Transaction newTransaction = mock(Transaction.class);
        when(newTransaction.getTransactionType()).thenReturn(newType);
        when(newTransaction.getPaid()).thenReturn(newPaid);
        when(newTransaction.getAmount()).thenReturn(newAmount);
        Account newAccount = mock(Account.class);
        when(newAccount.getId()).thenReturn(newAccountId);
        when(newTransaction.getAccount()).thenReturn(newAccount);

        event.setOldTransactionData(oldTransaction);
        event.setTransactionData(newTransaction);
        return event;
    }

    private Event createEventWithDate(TransactionType oldType, boolean oldPaid, BigDecimal oldAmount,
                                      TransactionType newType, boolean newPaid, BigDecimal newAmount, Long oldAccountId, Long newAccountId, LocalDate date) {
        Event event = createEvent(oldType, oldPaid, oldAmount, newType, newPaid, newAmount, oldAccountId, newAccountId);
        // Adicione lógica para configurar a data da transação, se necessário
        return event;
    }
}
