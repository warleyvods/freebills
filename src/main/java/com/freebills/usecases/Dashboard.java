package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardExpenseResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardGraphResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardRevenueResponseDTO;
import com.freebills.gateways.entities.Account;
import com.freebills.gateways.entities.Transaction;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
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

    public DashboardGraphResponseDTO getDonutsGraph(String login, Integer month, Integer year, TransactionType transactionType) {
        var transactions = getTransactionsByUserDateFilter(login, month, year)
                .stream()
                .filter(transaction -> transaction.getTransactionType() == transactionType)
                .filter(Transaction::isPaid)
                .toList();

        var transactionTypesLabels = transactions.stream()
                .map(Transaction::getTransactionCategory)
                .map(TransactionCategory::name)
                .distinct()
                .sorted()
                .toList();

        var values = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionCategory().name(),
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
                .filter(Account::isDashboard)
                .map(Account::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<Transaction> getTransactionsByUserDateFilter(String login, Integer month, Integer year) {
        return transactionGateway.findByUserDateFilter(login, month, year, null, null, null).getContent();
    }

    private BigDecimal getTotalAmountByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Boolean, BigDecimal> getAmountsByTypeAndPaidStatus(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(transaction -> transaction.getTransactionType() == type)
                .collect(groupingBy(Transaction::isPaid, reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
    }
}
