package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionUpdatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, final Event event) {
        // Dados da transação antiga
        BigDecimal oldTransactionAmount = event.getOldTransactionData().getAmount();
        TransactionType oldTransactionType = event.getOldTransactionData().getTransactionType();
        boolean oldTransactionPaid = event.getOldTransactionData().getPaid();
        Long oldAccountId = event.getOldTransactionData().getAccount().getId();

        // Dados da nova transação
        BigDecimal newTransactionAmount = event.getTransactionData().getAmount();
        TransactionType newTransactionType = event.getTransactionData().getTransactionType();
        boolean newTransactionPaid = event.getTransactionData().getPaid();
        Long newAccountId = event.getTransactionData().getAccount().getId();

        // Se a transação mudou de conta
        if (!oldAccountId.equals(newAccountId)) {
            // Reverter a transação da conta antiga
            if (oldTransactionPaid) {
                if (oldTransactionType == TransactionType.REVENUE) {
                    currentBalance = currentBalance.subtract(oldTransactionAmount);
                } else if (oldTransactionType == TransactionType.EXPENSE) {
                    currentBalance = currentBalance.add(oldTransactionAmount);
                }
            }

            // Aplicar a nova transação na nova conta
            if (newTransactionPaid) {
                if (newTransactionType == TransactionType.REVENUE) {
                    currentBalance = currentBalance.add(newTransactionAmount);
                } else if (newTransactionType == TransactionType.EXPENSE) {
                    currentBalance = currentBalance.subtract(newTransactionAmount);
                }
            }
        } else {
            // Se a transação não mudou de conta
            // Reverter a transação antiga se ela foi paga
            if (oldTransactionPaid) {
                if (oldTransactionType == TransactionType.REVENUE) {
                    currentBalance = currentBalance.subtract(oldTransactionAmount);
                } else if (oldTransactionType == TransactionType.EXPENSE) {
                    currentBalance = currentBalance.add(oldTransactionAmount);
                }
            }

            // Aplicar a nova transação se ela foi paga
            if (newTransactionPaid) {
                if (newTransactionType == TransactionType.REVENUE) {
                    currentBalance = currentBalance.add(newTransactionAmount);
                } else if (newTransactionType == TransactionType.EXPENSE) {
                    currentBalance = currentBalance.subtract(newTransactionAmount);
                }
            }
        }

        return currentBalance;
    }
}
