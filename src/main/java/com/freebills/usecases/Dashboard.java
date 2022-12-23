package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardExpenseResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardRevenueResponseDTO;
import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.groupingBy;

@Component
@AllArgsConstructor
public class Dashboard {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;

    public DashboardResponseDTO getTotalDashboard(final String login, final Integer month, final Integer year) {
        final var byUserDateFilter = getByUserDateFilter(login, month, year).getContent();
        final var results = byUserDateFilter.parallelStream()
                .collect(Collectors.groupingBy(Transaction::getTransactionType, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
        final var totalRevenueMonth = results.getOrDefault(TransactionType.REVENUE, BigDecimal.ZERO);
        final var totalExpenseMonth = results.getOrDefault(TransactionType.EXPENSE, BigDecimal.ZERO);
        return new DashboardResponseDTO(getTotalValue(login), totalRevenueMonth, totalExpenseMonth,  new BigDecimal(0));
    }

    public DashboardExpenseResponseDTO getDashboardExpense(final String login, final Integer month, final Integer year) {
        final var byUserDateFilter = getByUserDateFilter(login, month, year).getContent();
        final var results = byUserDateFilter.parallelStream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::isPaid, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
        final var totalExpensePending = results.getOrDefault(false, BigDecimal.ZERO);
        final var totalExpenseReciveid = results.getOrDefault(true, BigDecimal.ZERO);
        final var total = totalExpensePending.add(totalExpenseReciveid);
        return new DashboardExpenseResponseDTO(getTotalValue(login), totalExpensePending, totalExpenseReciveid,  total);
    }

    public DashboardRevenueResponseDTO getDashboardRevenue(final String login, final Integer month, final Integer year) {
        final var byUserDateFilter = getByUserDateFilter(login, month, year).getContent();
        final var results = byUserDateFilter.parallelStream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.REVENUE)
                .collect(Collectors.groupingBy(Transaction::isPaid, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
        final var totalRevenuePending = results.getOrDefault(false, BigDecimal.ZERO);
        final var totalRevenueReceived = results.getOrDefault(true, BigDecimal.ZERO);
        final var total = totalRevenuePending.add(totalRevenueReceived);
        return new DashboardRevenueResponseDTO(getTotalValue(login), totalRevenuePending, totalRevenueReceived, total);
    }

    private BigDecimal getTotalValue(String login) {
        return accountGateway.findByUserLogin(login)
                .stream()
                .filter(Account::isDashboard)
                .map(Account::getAmount)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
    }

    private Page<Transaction> getByUserDateFilter(String login, Integer month, Integer year) {
        return transactionGateway.findByUserDateFilter(login, month, year, null, null, null);
    }
}
