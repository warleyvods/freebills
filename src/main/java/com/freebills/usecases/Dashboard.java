package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public record Dashboard(AccountGateway accountGateway, TransactionGateway transactionGateway) {

    public DashboardResponseDTO totalBalanceById(final Long userId, final Integer month, final Integer year) {
        final var totalValue = accountGateway.findByUserId(userId)
                .stream()
                .filter(Account::isDashboard)
                .map(Account::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        final var totalRevenueMonth = transactionGateway.findByUserDateFilter(userId, month, year, null, null)
                .stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.REVENUE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        final var totalExpenseMonth = transactionGateway.findByUserDateFilter(userId, month, year, null, null)
                .stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        return new DashboardResponseDTO(totalValue, totalRevenueMonth, totalExpenseMonth,  new BigDecimal(0));
    }
}
