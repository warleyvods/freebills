package com.freebills.gateways;

import com.freebills.domains.CreditCard;
import com.freebills.repositories.CreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record CreditCardGateway(CreditCardRepository creditCardRepository) {

    public CreditCard save(final CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    public List<CreditCard> findByUserLogin(final String login) {
        return creditCardRepository.findByAccount_User_Login(login);
    }

    public CreditCard findById(final Long id){
        return creditCardRepository.findById(id).orElseThrow(()-> new NullPointerException("Credit card not found!"));
    }

    public CreditCard update(final CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
}
