package com.freebills.usecases;

import com.freebills.entities.CreditCard;
import com.freebills.gateways.CreditCardGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record FindCreditCard(CreditCardGateway creditCardGateway) {

    public List<CreditCard> execute(final String login) {
        return creditCardGateway.findByUserLogin(login);
    }

    public CreditCard byId(final Long id) {
        return creditCardGateway.findById(id);
    }
}
