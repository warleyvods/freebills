package com.freebills.usecases;

import com.freebills.domains.CreditCard;
import com.freebills.gateways.CreditCardGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCreditCart {

    private final CreditCardGateway creditCardGateway;

    public CreditCard execute(@Valid final CreditCard creditCard) {
        log.info("[SaveCreditCard:{}] Saving new Credit Card", creditCard.getDescription());
        return creditCardGateway.save(creditCard);
    }
}
