package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.UserGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record FindTransaction(TransactionGateway transactionGateway, UserGateway userGateway) {

    public Transaction findById(final Long id) {
        return transactionGateway.findById(id);
    }

    public List<Transaction> findAllByUser(final Long userId) {
        final User user = userGateway.findById(userId);
        return transactionGateway.findByUser(user);
    }

    public List<Transaction> findAllRevenueByUser(final Long userId) {
        final User user = userGateway.findById(userId);
        return transactionGateway.findByUser(user)
                .stream()
                .filter(ac -> ac.getTransactionType().equals(TransactionType.REVENUE))
                .toList();
    }

    public List<Transaction> findAllExpenseByUser(final Long userId) {
        final User user = userGateway.findById(userId);
        return transactionGateway.findByUser(user)
                .stream()
                .filter(ac -> ac.getTransactionType().equals(TransactionType.EXPENSE))
                .toList();
    }
}
