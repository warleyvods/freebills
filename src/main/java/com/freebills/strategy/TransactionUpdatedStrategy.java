package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionUpdatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, final Event event) {
        BigDecimal oldTransactionAmount = event.getOldTransactionAmount();
        BigDecimal newTransactionAmount = event.getNewTransactionAmount();
        TransactionType oldTransactionType = event.getOldTransactionType();
        TransactionType newTransactionType = event.getNewTransactionType();

        // Revert the old transaction
        if (oldTransactionType == TransactionType.REVENUE) {
            currentBalance = currentBalance.subtract(oldTransactionAmount);
        } else if (oldTransactionType == TransactionType.EXPENSE) {
            currentBalance = currentBalance.add(oldTransactionAmount);
        }

        // Apply the new transaction
        if (newTransactionType == TransactionType.REVENUE) {
            currentBalance = currentBalance.add(newTransactionAmount);
        } else if (newTransactionType == TransactionType.EXPENSE) {
            currentBalance = currentBalance.subtract(newTransactionAmount);
        }

        return currentBalance;
    }
}