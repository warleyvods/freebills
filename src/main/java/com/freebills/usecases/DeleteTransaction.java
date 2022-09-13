package com.freebills.usecases;

import com.freebills.gateways.TransactionGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteTransaction(TransactionGateway transactionGateway) {

    public void delete(final Long id) {
        log.info("[deleteAccount:{}] Deleting a account", id);
        transactionGateway.delete(id);
    }
}
