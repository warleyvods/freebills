package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateAccount {

    private final AccountGateway accountGateway;

    public Account create(@Valid final Account account) {
        log.info("[CreateAccount:{}] Creating new account", account.getUser().getEmail());
        return accountGateway.save(account);
    }
}
