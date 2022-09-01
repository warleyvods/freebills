package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record FindTransaction(TransactionGateway transactionGateway) {

    public Transaction findById(final Long id) {
        return transactionGateway.findById(id);
    }

    public List<Transaction> findAllById(final Long id) {
        return transactionGateway.findByAccount_Id(id);
    }
}
