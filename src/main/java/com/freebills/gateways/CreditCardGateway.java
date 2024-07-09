package com.freebills.gateways;

import com.freebills.domain.CreditCard;
import com.freebills.exceptions.CreditCardNotFoundException;
import com.freebills.gateways.mapper.CreditCardGatewayMapper;
import com.freebills.repositories.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardGateway {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardGatewayMapper mapper;

    public CreditCard save(final CreditCard creditCard) {
        return mapper.toDomain(creditCardRepository.save(mapper.toEntity(creditCard)));
    }

    public List<CreditCard> findAllByUsers(final String login) {
        return creditCardRepository.findByAccount_User_Login(login).stream().map(mapper::toDomain).toList();
    }

    public CreditCard findById(final Long id, final String username) {
        return mapper.toDomain(creditCardRepository.findByIdAndAccount_User_Login(id, username)
                .orElseThrow(() -> new CreditCardNotFoundException("credit card not found!")));
    }

    public CreditCard update(final CreditCard creditCard) {
        return mapper.toDomain(creditCardRepository.save(mapper.toEntity(creditCard)));
    }

    public void delete(final Long id, final String username) {
        creditCardRepository.deleteByIdAndAccount_User_Login(id, username);
    }
}
