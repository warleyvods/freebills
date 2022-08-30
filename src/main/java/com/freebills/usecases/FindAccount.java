package com.freebills.usecases;


import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record FindAccount(AccountGateway accountService) {

    public Account byId(final Long id){
        return accountService.findById(id);
    }

    public List<Account> all(){
        return accountService.findAll();
    }
}
