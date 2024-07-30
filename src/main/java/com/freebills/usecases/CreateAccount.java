package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Transaction;
import com.freebills.events.account.AccountCreatedEvent;
import com.freebills.gateways.AccountGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateAccount {

    private final AccountGateway accountGateway;
    private final VerifyPrincipal isPrincipal;
    private final ApplicationEventPublisher eventPublisher;

    public Account create(@Valid final Account account, final String username) {
        isPrincipal.execute(account.getUser().getId(), username);
        log.info("[CreateAccount:{}] Creating new account", account.getUser().getEmail());

        final Account savedAccount = accountGateway.save(account);

        eventPublisher.publishEvent(new AccountCreatedEvent(this, savedAccount.getId(), new Transaction(account.getAmount())));
        return savedAccount;
    }
}
