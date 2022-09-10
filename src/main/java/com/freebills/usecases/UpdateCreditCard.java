package com.freebills.usecases;

import com.freebills.domains.CreditCard;
import com.freebills.gateways.CreditCardGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateCreditCard(CreditCardGateway creditCardGateway) {

    public CreditCard update(final CreditCard creditCard) {
        return creditCardGateway.update(creditCard);
    }
}
