package com.freebills.usecases;

import com.freebills.gateways.AccountGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteAccount(AccountGateway accountGateway) {

    public void deleteAccount(final Long accId) {
        log.info("[deleteAccount:{}] Deleting a account", accId);
        accountGateway.deleteById(accId);
    }
}
