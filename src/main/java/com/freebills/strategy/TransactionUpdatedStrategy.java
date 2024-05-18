package com.freebills.strategy;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionUpdatedStrategy implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        BigDecimal oldTransactionAmount = event.getOldTransactionAmount();
        BigDecimal newTransactionAmount = event.getNewTransactionAmount();
        TransactionType oldTransactionType = event.getOldTransactionType();
        TransactionType newTransactionType = event.getNewTransactionType();

        // Verifica se houve alteração no valor ou no tipo da transação
        if (!oldTransactionAmount.equals(newTransactionAmount) || !oldTransactionType.equals(newTransactionType)) {
            // Se houve mudança de despesa para receita, subtrai o valor antigo do saldo
            if (oldTransactionType == TransactionType.EXPENSE) {
                currentBalance = currentBalance.subtract(oldTransactionAmount);
            }
            // Se houve mudança de receita para despesa, adiciona o valor novo ao saldo
            if (newTransactionType == TransactionType.EXPENSE) {
                currentBalance = currentBalance.add(newTransactionAmount);
            }
        } else {
            // Se o tipo de transação permaneceu o mesmo, ajusta o saldo pela diferença entre os valores antigo e novo
            BigDecimal balanceChange = newTransactionAmount.subtract(oldTransactionAmount);
            currentBalance = currentBalance.add(balanceChange);
        }

        return currentBalance;
    }
}