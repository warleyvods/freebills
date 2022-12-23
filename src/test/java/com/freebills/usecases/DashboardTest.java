package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import com.freebills.domains.enums.AccountType;
import com.freebills.domains.enums.BankType;
import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DashboardTest {

    @Autowired
    private Dashboard dashboard;

    @Autowired
    private CreateTransaction createTransaction;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static Account account;

    @Autowired
    private UserRepository userRepository;

    private List<Transaction> transactionMockList() {
        Transaction t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.REVENUE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(true);
        t1.setAccount(account);

        Transaction t2 = new Transaction();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.REVENUE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(false);
        t2.setAccount(account);

        Transaction t3 = new Transaction();
        t3.setAmount(new BigDecimal("100"));
        t3.setDate(LocalDate.of(2022, 1, 1));
        t3.setDescription("Arroz");
        t3.setBarCode(null);
        t3.setBankSlip(false);
        t3.setTransactionType(TransactionType.EXPENSE);
        t3.setTransactionCategory(TransactionCategory.HOUSE);
        t3.setPaid(true);
        t3.setAccount(account);

        Transaction t4 = new Transaction();
        t4.setAmount(new BigDecimal("100"));
        t4.setDate(LocalDate.of(2022, 1, 1));
        t4.setDescription("Arroz");
        t4.setBarCode(null);
        t4.setBankSlip(false);
        t4.setTransactionType(TransactionType.EXPENSE);
        t4.setTransactionCategory(TransactionCategory.HOUSE);
        t4.setPaid(false);
        t4.setAccount(account);

        return List.of(t1, t2, t3, t4);
    }

    @BeforeEach
    void beforeSetup() {
        User user = userRepository.findById(1L).orElse(null);

        final var acc01 = new Account();
        acc01.setAmount(new BigDecimal("0"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(user);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        account = accountsRepository.save(acc01);
    }

    @AfterEach
    void afterSetup() {
        accountsRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void testGetTotalDashboard() {
        transactionMockList().forEach(data -> createTransaction.execute(data));

        DashboardResponseDTO result = dashboard.getTotalDashboard("admin", 1, 2022);

        assertEquals(0, new BigDecimal(0).compareTo(result.totalBalance()));
        assertEquals(0, new BigDecimal(200).compareTo(result.totalRevenue()));
        assertEquals(0, new BigDecimal(200).compareTo(result.totalExpensive()));
        assertEquals(0, new BigDecimal(0).compareTo(result.totalExpensiveCards()));
    }

    @Test
    void testGetTotalDashboardRevenue() {
        transactionMockList().forEach(data -> createTransaction.execute(data));

        final var result = dashboard.getDashboardRevenue("admin", 1, 2022);

        assertEquals(0, new BigDecimal(0).compareTo(result.totalBalance()));
        assertEquals(0, new BigDecimal(100).compareTo(result.totalRevenuePending()));
        assertEquals(0, new BigDecimal(100).compareTo(result.totalRevenueReceived()));
        assertEquals(0, new BigDecimal(200).compareTo(result.totalRevenue()));
    }

    @Test
    void testGetTotalDashboardExpense() {
        transactionMockList().forEach(data -> createTransaction.execute(data));

        final var result = dashboard.getDashboardExpense("admin", 1, 2022);

        assertEquals(0, new BigDecimal(0).compareTo(result.totalBalance()));
        assertEquals(0, new BigDecimal(100).compareTo(result.totalExpensePending()));
        assertEquals(0, new BigDecimal(100).compareTo(result.totalExpenseReceived()));
        assertEquals(0, new BigDecimal(200).compareTo(result.totalExpense()));
    }

    @Test
    void testGetTotalValue() {
        User user = userRepository.findById(1L).orElse(null);

        final var acc01 = new Account();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(user);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final var acc02 = new Account();
        acc02.setAmount(new BigDecimal("200"));
        acc02.setDescription("Conta Inter");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.INTER);
        acc02.setUser(user);
        acc02.setArchived(false);
        acc02.setDashboard(false);

        accountsRepository.save(acc01);
        accountsRepository.save(acc02);

        final var response = dashboard.getTotalDashboard("admin", 1, 2022);

        assertEquals(0, new BigDecimal(0).compareTo(response.totalBalance()));
    }

    @Test
    void testGetTotalValueWhenDashboardAreTrue() {
        User user = userRepository.findById(1L).orElse(null);

        final var acc01 = new Account();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(user);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final var acc02 = new Account();
        acc02.setAmount(new BigDecimal("200"));
        acc02.setDescription("Conta Inter");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.INTER);
        acc02.setUser(user);
        acc02.setArchived(false);
        acc02.setDashboard(true);

        accountsRepository.save(acc01);
        accountsRepository.save(acc02);

        final var response = dashboard.getTotalDashboard("admin", 1, 2022);

        assertEquals(0, new BigDecimal(200).compareTo(response.totalBalance()));
    }
}