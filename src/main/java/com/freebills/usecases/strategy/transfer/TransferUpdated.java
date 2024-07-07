package com.freebills.usecases.strategy.transfer;

import com.freebills.domain.Event;
import com.freebills.domain.Transfer;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;

@Component(value = "TRANSFER_UPDATED")
public class TransferUpdated implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, final Event event) {
        final Transfer oldTransferData = event.getOldTransferData();
        final Transfer newTransferData = event.getTransferData();

        currentBalance = revertOldTransfer(currentBalance, oldTransferData);
        currentBalance = applyNewTransfer(currentBalance, newTransferData);

        return currentBalance;
    }

    private BigDecimal revertOldTransfer(final BigDecimal currentBalance, final Transfer oldTransfer) {
        final var amount = oldTransfer.getAmount();

        if (oldTransfer.getTransferType().equals(IN)) {
            return currentBalance.subtract(amount);
        } else if (oldTransfer.getTransferType().equals(OUT)) {
            return currentBalance.add(amount);
        }

        return currentBalance;
    }

    private BigDecimal applyNewTransfer(final BigDecimal currentBalance, final Transfer newTransfer) {
        final var amount = newTransfer.getAmount();

        if (newTransfer.getTransferType().equals(IN)) {
            return currentBalance.add(amount);
        } else if (newTransfer.getTransferType().equals(OUT)) {
            return currentBalance.subtract(amount);
        }

        return currentBalance;
    }
}
