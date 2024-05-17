package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.AccountGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record UpdateAccount(AccountGateway accountGateway) {

    public Account update(final Account account) {
        final var update = accountGateway.update(account);
        log.info("[updateAccount:{}] Update a account", update.getId());
        return update;
    }
}
