package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(value = "TRANSACTION_UPDATED")
public class TransactionUpdatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, final Event event) {
        var oldTransactionData = event.getOldTransactionData();
        var newTransactionData = event.getTransactionData();

        currentBalance = adjustBalance(currentBalance, oldTransactionData, false);
        currentBalance = adjustBalance(currentBalance, newTransactionData, true);

        return currentBalance;
    }

    private BigDecimal adjustBalance(BigDecimal currentBalance, Transaction transactionData, boolean isAddition) {
        BigDecimal transactionAmount = transactionData.getAmount();
        TransactionType transactionType = transactionData.getTransactionType();
        boolean transactionPaid = transactionData.getPaid();

        if (transactionPaid) {
            if (transactionType == TransactionType.REVENUE) {
                currentBalance = isAddition ? currentBalance.add(transactionAmount) : currentBalance.subtract(transactionAmount);
            } else if (transactionType == TransactionType.EXPENSE) {
                currentBalance = isAddition ? currentBalance.subtract(transactionAmount) : currentBalance.add(transactionAmount);
            }
        }

        return currentBalance;
    }
}
