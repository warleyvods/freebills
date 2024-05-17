package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.AccountGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateAccount {

    private final AccountGateway accountGateway;
    private final VerifyPrincipal isPrincipal;

    public Account create(@Valid final Account account, final Principal principal) {
        isPrincipal.execute(account.getUser().getId(), principal);
        log.info("[CreateAccount:{}] Creating new account", account.getUser().getEmail());
        return accountGateway.save(account);
    }
}
