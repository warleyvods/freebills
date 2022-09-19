package com.freebills.usecases;

import com.freebills.domains.CreditCard;
import com.freebills.gateways.CreditCardGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Slf4j
@Component
public record CreateCreditCart(CreditCardGateway creditCardGateway) {

    public CreditCard save(@Valid final CreditCard creditCard) {
        log.info("[SaveCreditCard:{}] Saving new Credit Card", creditCard.getDescription());
        return creditCardGateway.save(creditCard);
    }
}
