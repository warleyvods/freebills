package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.AccountGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateAccount {

    private final AccountGateway accountGateway;

    public Account update(final Account account) {
        final var update = accountGateway.update(account);
        log.info("[updateAccount:{}] Update a account", update.getId());
        return update;
    }
}
