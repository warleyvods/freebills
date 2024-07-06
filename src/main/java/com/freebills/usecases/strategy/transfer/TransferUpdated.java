package com.freebills.usecases.strategy.transfer;

import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.domain.Transfer;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.EventType.TRANSFER_DELETED;
import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;
import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;
import static java.lang.Boolean.TRUE;

@Component(value = "TRANSFER_UPDATED")
public class TransferUpdated implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        final Transfer oldTransferData = event.getOldTransferData();
        final Transfer newTransferData = event.getTransferData();

        currentBalance = revertOldTransfer(currentBalance, oldTransferData);
        currentBalance = applyNewTransfer(currentBalance, newTransferData);

        return currentBalance;
    }

    private BigDecimal revertOldTransfer(BigDecimal currentBalance, Transfer oldTransfer) {
        BigDecimal amount = oldTransfer.getAmount();

        if (oldTransfer.getTransferType().equals(IN)) {
            return currentBalance.subtract(amount);
        } else if (oldTransfer.getTransferType().equals(OUT)) {
            return currentBalance.add(amount);
        }

        return currentBalance;
    }

    private BigDecimal applyNewTransfer(BigDecimal currentBalance, Transfer newTransfer) {
        BigDecimal amount = newTransfer.getAmount();

        if (newTransfer.getTransferType().equals(IN)) {
            return currentBalance.add(amount);
        } else if (newTransfer.getTransferType().equals(OUT)) {
            return currentBalance.subtract(amount);
        }

        return currentBalance;
    }
}
