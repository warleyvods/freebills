package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.UserGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public record FindTransaction(TransactionGateway transactionGateway, UserGateway userGateway) {

    public Transaction findById(final Long id) {
        return transactionGateway.findById(id);
    }

    public Page<Transaction> findAllByUser(final Long userId, final Pageable pageable) {
        final User user = userGateway.findById(userId);
        return transactionGateway.findByUser(user, pageable);
    }

    public Page<Transaction> findAllByUserDateFilter(String login, Integer month, Integer year, final Pageable pageable, String keyword) {
        return transactionGateway.findByUserDateFilter(login, month, year, pageable, keyword);
    }

    public Page<Transaction> findAllRevenueByUser(final Long userId, final Pageable pageable) {
        final User user = userGateway.findById(userId);
        final var transactions = transactionGateway.findByUser(user, pageable)
                .stream()
                .filter(ac -> ac.getTransactionType().equals(TransactionType.REVENUE))
                .toList();

        return new PageImpl<>(transactions);
    }

    public Page<Transaction> findAllExpenseByUser(final Long userId, final Pageable pageable) {
        final User user = userGateway.findById(userId);
        final var transactions = transactionGateway.findByUser(user, pageable)
                .stream()
                .filter(ac -> ac.getTransactionType().equals(TransactionType.EXPENSE))
                .toList();

        return new PageImpl<>(transactions);
    }
}
