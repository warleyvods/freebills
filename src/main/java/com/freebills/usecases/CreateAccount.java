package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Slf4j
@Component
public record CreateAccount(AccountGateway accountGateway) {

    public Account create(@Valid final Account account){
        log.info("[CreateAccount:{}] Creating new account", account.getUser());
        return accountGateway.save(account);
    }
}
