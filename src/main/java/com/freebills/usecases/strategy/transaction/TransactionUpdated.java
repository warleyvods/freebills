package com.freebills.usecases.strategy.transaction;

import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;
import static java.lang.Boolean.TRUE;

@Component(value = "TRANSACTION_UPDATED")
public class TransactionUpdated implements BalanceUpdateStrategy {

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

        if (TRUE.equals(transactionData.isPaid())) {
            if (transactionData.getTransactionType() == REVENUE) {
                currentBalance = isAddition ? currentBalance.add(transactionAmount) : currentBalance.subtract(transactionAmount);
            } else if (transactionData.getTransactionType() == EXPENSE) {
                currentBalance = isAddition ? currentBalance.subtract(transactionAmount) : currentBalance.add(transactionAmount);
            }
        }

        return currentBalance;
    }
}
