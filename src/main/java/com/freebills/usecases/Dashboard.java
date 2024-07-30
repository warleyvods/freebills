package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardExpenseResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardGraphResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardRevenueResponseDTO;
import com.freebills.domain.Account;
import com.freebills.domain.Category;
import com.freebills.domain.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
@AllArgsConstructor
public class Dashboard {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;
    private final AccountBalanceCalculator accountBalanceCalculator;

    public DashboardGraphResponseDTO getDonutsGraph(String login, Integer month, Integer year, TransactionType transactionType) {
        var transactions = getTransactionsByUserDateFilter(login, month, year)
                .stream()
                .filter(transaction -> transaction.getTransactionType() == transactionType)
                .filter(Transaction::getPaid)
                .toList();

        var transactionTypesLabels = transactions.stream()
                .map(Transaction::getCategory)
                .map(Category::getName)
                .distinct()
                .sorted()
                .toList();

        var values = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted(Comparator.comparingInt(t -> transactionTypesLabels.indexOf(t.getKey())))
                .map(Map.Entry::getValue)
                .toList();

        return new DashboardGraphResponseDTO(transactionTypesLabels, values);
    }

    public DashboardResponseDTO getTotalDashboard(String login, Integer month, Integer year) {
        final var transactions = getTransactionsByUserDateFilter(login, month, year);
        final var totalRevenue = getTotalAmountByType(transactions, TransactionType.REVENUE);
        final var totalExpense = getTotalAmountByType(transactions, TransactionType.EXPENSE);
        return new DashboardResponseDTO(getTotalValue(login), totalRevenue, totalExpense, BigDecimal.ZERO);
    }


    public DashboardExpenseResponseDTO getDashboardExpense(String login, Integer month, Integer year) {
        final var transactions = getTransactionsByUserDateFilter(login, month, year);
        final var amountsByPaidStatus = getAmountsByTypeAndPaidStatus(transactions, TransactionType.EXPENSE);
        final var totalExpensePending = amountsByPaidStatus.getOrDefault(false, BigDecimal.ZERO);
        final var totalExpenseReceived = amountsByPaidStatus.getOrDefault(true, BigDecimal.ZERO);
        final var totalExpense = totalExpensePending.add(totalExpenseReceived);
        return new DashboardExpenseResponseDTO(getTotalValue(login), totalExpensePending, totalExpenseReceived, totalExpense);
    }

    public DashboardRevenueResponseDTO getDashboardRevenue(String login, Integer month, Integer year) {
        final var transactions = getTransactionsByUserDateFilter(login, month, year);
        final var amountsByPaidStatus = getAmountsByTypeAndPaidStatus(transactions, TransactionType.REVENUE);
        final var totalRevenuePending = amountsByPaidStatus.getOrDefault(false, BigDecimal.ZERO);
        final var totalRevenueReceived = amountsByPaidStatus.getOrDefault(true, BigDecimal.ZERO);
        final var totalRevenue = totalRevenuePending.add(totalRevenueReceived);
        return new DashboardRevenueResponseDTO(getTotalValue(login), totalRevenuePending, totalRevenueReceived, totalRevenue);
    }

    private BigDecimal getTotalValue(String login) {
        return accountGateway.findByUserLogin(login)
                .stream()
                .filter(Account::getDashboard)
                .map(this::calculateBalanceForSingleAccount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<Transaction> getTransactionsByUserDateFilter(String login, Integer month, Integer year) {
        return transactionGateway.findTransactionsWithFilters(login, month, year, null, null, null).getContent();
    }

    private BigDecimal getTotalAmountByType(List<Transaction> transactionEntities, TransactionType type) {
        return transactionEntities.stream()
                .filter(t -> t.getTransactionType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Boolean, BigDecimal> getAmountsByTypeAndPaidStatus(List<Transaction> transactionEntities, TransactionType type) {
        return transactionEntities.stream()
                .filter(transaction -> transaction.getTransactionType() == type)
                .collect(groupingBy(Transaction::getPaid, reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
    }

    private BigDecimal calculateBalanceForSingleAccount(Account account) {
        return accountBalanceCalculator.calculateBalanceForAccount(account);
    }
}
