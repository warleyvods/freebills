package com.freebills.usecases.strategy.transaction;

import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_TRANSFER;

@Component(value = "TRANSACTION_TRANSFER")
public class TransactionTransfer implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, final Event event) {
        if (event.getEventType() != TRANSACTION_TRANSFER) {
            return currentBalance;
        }

        final Transaction transactionData = event.getTransactionData();
        final Transaction oldTransactionData = event.getOldTransactionData();

        currentBalance = adjustOldTransaction(currentBalance, oldTransactionData);

        return currentBalance;
    }

    private BigDecimal adjustOldTransaction(BigDecimal currentBalance, Transaction transactionData) {
        BigDecimal transactionAmount = transactionData.getAmount();

        if (Boolean.TRUE.equals(transactionData.isPaid())) {
            if (transactionData.getTransactionType() == REVENUE) {
                currentBalance = currentBalance.subtract(transactionAmount);
            } else if (transactionData.getTransactionType() == EXPENSE) {
                currentBalance = currentBalance.add(transactionAmount);
            }
        }
        return currentBalance;
    }
}

