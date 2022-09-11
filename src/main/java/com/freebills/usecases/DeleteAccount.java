package com.freebills.usecases;

import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

@Component
public record DeleteAccount(AccountGateway accountGateway) {

    public void deleteAccount(final Long accId) {
        accountGateway.deleteById(accId);
    }
}
