package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.UserGateway;
import org.springframework.data.domain.Page;
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
        return transactionGateway.findByUserDateFilterRevenue(login, month, year, pageable, keyword);
    }

    public Page<Transaction> findAllExpenseByUser(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        return transactionGateway.findByUserDateFilterExpense(login, month, year, pageable, keyword);
    }
}
