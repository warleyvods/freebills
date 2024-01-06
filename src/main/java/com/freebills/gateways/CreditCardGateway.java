package com.freebills.gateways;

import com.freebills.gateways.entities.CreditCard;
import com.freebills.exceptions.CreditCardNotFoundException;
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
        return creditCardRepository.findById(id).orElseThrow(()-> new CreditCardNotFoundException("Credit card not found!"));
    }

    public CreditCard update(final CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    public void delete(final Long id) {
        creditCardRepository.deleteById(id);
    }
}
