package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Transaction;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.AccountGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.security.Principal;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateAccount {

    private final AccountGateway accountGateway;
    private final VerifyPrincipal isPrincipal;
    private final ApplicationEventPublisher eventPublisher;

    public Account create(@Valid final Account account, final Principal principal) {
        isPrincipal.execute(account.getUser().getId(), principal);
        log.info("[CreateAccount:{}] Creating new account", account.getUser().getEmail());

        final Account savedAccount = accountGateway.save(account);

        eventPublisher.publishEvent(new TransactionCreatedEvent(this, savedAccount.getId(), new Transaction(account.getAmount())));
        return account;
    }
}
