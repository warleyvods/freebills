package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public record Dashboard(AccountGateway accountGateway, TransactionGateway transactionGateway) {

    public DashboardResponseDTO totalBalanceById(final String login, final Integer month, final Integer year) {

        final var totalValue = accountGateway.findByUserLogin(login)
                .stream()
                .filter(Account::isDashboard)
                .map(Account::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        final var byUserDateFilter = getByUserDateFilter(login, month, year).getContent();

        final var totalRevenueMonth = byUserDateFilter.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.REVENUE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        final var totalExpenseMonth = byUserDateFilter.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        return new DashboardResponseDTO(totalValue, totalRevenueMonth, totalExpenseMonth,  new BigDecimal(0));
    }

    private Page<Transaction> getByUserDateFilter(String login, Integer month, Integer year) {
        return transactionGateway.findByUserDateFilter(login, month, year, null, null);
    }
}
