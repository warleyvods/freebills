package com.freebills.usecases;

import com.freebills.domain.CreditCard;
import com.freebills.gateways.CreditCardGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCreditCard {

    private final CreditCardGateway creditCardGateway;

    public List<CreditCard> findAll(final boolean archived, final String username) {
        return creditCardGateway.findAllByUsers(archived, username);
    }

    public CreditCard byId(final Long id, final String username) {
        return creditCardGateway.findById(id, username);
    }

    public CreditCard byId(final Long id) {
        return creditCardGateway.findById(id);
    }
}
