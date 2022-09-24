package com.freebills.usecases;

import com.freebills.gateways.CreditCardGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteCard(CreditCardGateway creditCardGateway) {

    public void execute(final Long ccId) {
        log.info("[deleteCC:{}] Deleting a card", ccId);
        creditCardGateway.delete(ccId);
    }
}
