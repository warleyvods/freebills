package com.freebills.usecases.strategy.transfer;

import com.freebills.domain.Event;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.EventType.TRANSFER_DELETED;
import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;

@Component(value = "TRANSFER_DELETED")
public class TransferDeleted implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        if (isTransferIn(event)) {
            return currentBalance.add(event.getTransferData().getAmount());
        } else if (isTransferOut(event)) {
            return currentBalance.subtract(event.getTransferData().getAmount());
        }

        return currentBalance;
    }

    private boolean isTransferIn(Event event) {
        return event.getEventType() == TRANSFER_DELETED && event.getTransferData().getTransferType() == IN;
    }

    private boolean isTransferOut(Event event) {
        return event.getEventType() == TRANSFER_DELETED && event.getTransferData().getTransferType() == OUT;
    }
}
