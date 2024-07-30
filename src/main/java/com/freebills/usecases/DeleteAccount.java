package com.freebills.usecases;

import com.freebills.gateways.AccountGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteAccount {

    private final AccountGateway accountGateway;

    public void deleteAccount(final Long accId) {
        log.info("[delete-account:{}] Deleting a account", accId);
        accountGateway.deleteById(accId);
    }
}
