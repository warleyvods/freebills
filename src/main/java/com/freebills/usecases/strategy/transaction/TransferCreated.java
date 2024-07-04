package com.freebills.usecases.strategy.transaction;

import com.freebills.domain.Event;
import com.freebills.usecases.strategy.BalanceUpdateStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.EventType.TRANSFER;
import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;

@Component(value = "TRANSFER")
public class TransferCreated implements BalanceUpdateStrategy {

    @Override
    public BigDecimal updateBalance(BigDecimal currentBalance, Event event) {
        if (isTransferIn(event)) {
            return currentBalance.add(event.getAmount());
        } else if (isTransferOut(event)) {
            return currentBalance.subtract(event.getAmount());
        }

        return currentBalance;
    }

    private boolean isTransferIn(Event event) {
        return event.getEventType() == TRANSFER && event.getTransferType() == IN;
    }

    private boolean isTransferOut(Event event) {
        return event.getEventType() == TRANSFER && event.getTransferType() == OUT;
    }
}
