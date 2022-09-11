package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateAccount(AccountGateway accountGateway) {

    public Account update(final Account account){
        return accountGateway.update(account);
    }
}
