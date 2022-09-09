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

    public Page<Transaction> findAllByUserDateFilter(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        return transactionGateway.findByUserDateFilter(login, month, year, pageable, keyword);
    }

    public Page<Transaction> findAllRevenueByUser(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        final var transactions = transactionGateway.findByUserDateFilter(login, month, year, pageable, keyword)
                .stream()
                .filter(ac -> ac.getTransactionType().equals(TransactionType.REVENUE))
                .toList();

        return new PageImpl<>(transactions);
    }

    public Page<Transaction> findAllExpenseByUser(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        final var transactions = transactionGateway.findByUserDateFilter(login, month, year, pageable, keyword)
                .stream()
                .filter(ac -> ac.getTransactionType().equals(TransactionType.EXPENSE))
                .toList();

        return new PageImpl<>(transactions);
    }
}
