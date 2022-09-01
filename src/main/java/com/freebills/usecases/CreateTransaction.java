package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Slf4j
@Component
public record CreateTransaction(TransactionGateway transactionGateway) {

    public Transaction create(@Valid final Transaction transaction) {
        return transactionGateway.save(transaction);
    }

}
