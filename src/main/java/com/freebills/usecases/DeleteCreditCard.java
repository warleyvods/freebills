package com.freebills.usecases;

import com.freebills.gateways.CreditCardGateway;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCreditCard {

    private final CreditCardGateway creditCardGateway;
    private final FindCreditCard findCreditCard;

    @Transactional
    public void execute(final Long id, final String username) {
        findCreditCard.byId(id, username);
        creditCardGateway.delete(id, username);
    }
}
