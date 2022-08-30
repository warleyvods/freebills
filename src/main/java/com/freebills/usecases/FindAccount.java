package com.freebills.usecases;


import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record FindAccount(AccountGateway accountGateway) {

    public Account byId(final Long id){
        return accountGateway.findById(id);
    }

    public List<Account> findByUserId(final Long id){
        return accountGateway.findByUserId(id);
    }
}
