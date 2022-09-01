package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record FindTransaction(TransactionGateway transactionGateway) {

    public Transaction byId(final Long id) {
        return transactionGateway.findById(id);
    }

    public List<Transaction> findAll() {
        return transactionGateway.findAll();
    }
}
