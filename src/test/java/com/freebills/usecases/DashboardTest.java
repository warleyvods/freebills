package com.freebills.usecases;

import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.domain.Account;
import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.mapper.AccountGatewayMapper;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import com.freebills.utils.TestContainerBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@Disabled
@TestInstance(PER_CLASS)
class DashboardTest extends TestContainerBase {

    @Autowired
    private Dashboard dashboard;

    @Autowired
    private CreateTransaction createTransaction;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountGatewayMapper accountGatewayMapper;

    private static Account account;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeSetup() {
        UserEntity userEntity = userRepository.findById(1L).orElse(null);

        final var acc01 = new AccountEntity();
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        account = accountGatewayMapper.toDomain(accountsRepository.save(acc01));
    }

    @AfterEach
    void afterSetup() {
        accountsRepository.deleteAll();
        transactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<Transaction> transactionMockList() {
        var t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.REVENUE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(true);
        t1.setAccount(account);

        var t2 = new Transaction();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.REVENUE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(false);
        t2.setAccount(account);

        var t3 = new Transaction();
        t3.setAmount(new BigDecimal("100"));
        t3.setDate(LocalDate.of(2022, 1, 1));
        t3.setDescription("Arroz");
        t3.setBarCode(null);
        t3.setBankSlip(false);
        t3.setTransactionType(TransactionType.EXPENSE);
        t3.setTransactionCategory(TransactionCategory.HOUSE);
        t3.setPaid(true);
        t3.setAccount(account);

        var t4 = new Transaction();
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

    private List<Transaction> transactionMockList2() {
        var t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.REVENUE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(true);
        t1.setAccount(account);

        var t2 = new Transaction();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.REVENUE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(false);
        t2.setAccount(account);

        var t3 = new Transaction();
        t3.setAmount(new BigDecimal("100"));
        t3.setDate(LocalDate.of(2023, 1, 1));
        t3.setDescription("Arroz");
        t3.setBarCode(null);
        t3.setBankSlip(false);
        t3.setTransactionType(TransactionType.EXPENSE);
        t3.setTransactionCategory(TransactionCategory.HOUSE);
        t3.setPaid(true);
        t3.setAccount(account);

        var t4 = new Transaction();
        t4.setAmount(new BigDecimal("100"));
        t4.setDate(LocalDate.of(2023, 1, 1));
        t4.setDescription("Arroz");
        t4.setBarCode(null);
        t4.setBankSlip(false);
        t4.setTransactionType(TransactionType.EXPENSE);
        t4.setTransactionCategory(TransactionCategory.HOUSE);
        t4.setPaid(false);
        t4.setAccount(account);

        return List.of(t1, t2, t3, t4);
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
    void testGetTotalDashboardByYear() {
        transactionMockList2().forEach(data -> createTransaction.execute(data));

        DashboardResponseDTO result = dashboard.getTotalDashboardByYear("admin",  2022);

        assertEquals(0, new BigDecimal(0).compareTo(result.totalBalance()));
        assertEquals(0, new BigDecimal(200).compareTo(result.totalRevenue()));
        assertEquals(0, new BigDecimal(0).compareTo(result.totalExpensive()));
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
        UserEntity userEntity = userRepository.findById(1L).orElse(null);

        final var acc01 = new AccountEntity();
//        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final var acc02 = new AccountEntity();
//        acc02.setAmount(new BigDecimal("200"));
        acc02.setDescription("Conta Inter");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.INTER);
        acc02.setUser(userEntity);
        acc02.setArchived(false);
        acc02.setDashboard(false);

        accountsRepository.save(acc01);
        accountsRepository.save(acc02);

        final var response = dashboard.getTotalDashboard("admin", 1, 2022);

        assertEquals(0, new BigDecimal(0).compareTo(response.totalBalance()));
    }

    @Test
    @Disabled
    void testGetTotalValueWhenDashboardAreTrue() {
        UserEntity userEntity = userRepository.findById(1L).orElse(null);

        final var acc01 = new AccountEntity();
//        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final var acc02 = new AccountEntity();
//        acc02.setAmount(new BigDecimal("200"));
        acc02.setDescription("Conta Inter");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.INTER);
        acc02.setUser(userEntity);
        acc02.setArchived(false);
        acc02.setDashboard(true);

        accountsRepository.save(acc01);
        accountsRepository.save(acc02);

        final var response = dashboard.getTotalDashboard("admin", 1, 2022);

        assertEquals(0, new BigDecimal(200).compareTo(response.totalBalance()));
    }

    private List<Transaction> getTransactionMockListGraph() {
        var t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.EXPENSE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(true);
        t1.setAccount(account);

        var t2 = new Transaction();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.EXPENSE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(true);
        t2.setAccount(account);

        var t3 = new Transaction();
        t3.setAmount(new BigDecimal("200"));
        t3.setDate(LocalDate.of(2022, 1, 1));
        t3.setDescription("Arroz");
        t3.setBarCode(null);
        t3.setBankSlip(false);
        t3.setTransactionType(TransactionType.EXPENSE);
        t3.setTransactionCategory(TransactionCategory.EDUCATION);
        t3.setPaid(true);
        t3.setAccount(account);

        var t4 = new Transaction();
        t4.setAmount(new BigDecimal("200"));
        t4.setDate(LocalDate.of(2022, 1, 1));
        t4.setDescription("Arroz");
        t4.setBarCode(null);
        t4.setBankSlip(false);
        t4.setTransactionType(TransactionType.EXPENSE);
        t4.setTransactionCategory(TransactionCategory.EDUCATION);
        t4.setPaid(true);
        t4.setAccount(account);

        var t5 = new Transaction();
        t5.setAmount(new BigDecimal("400"));
        t5.setDate(LocalDate.of(2022, 1, 1));
        t5.setDescription("Arroz");
        t5.setBarCode(null);
        t5.setBankSlip(false);
        t5.setTransactionType(TransactionType.EXPENSE);
        t5.setTransactionCategory(TransactionCategory.TRANSPORT);
        t5.setPaid(true);
        t5.setAccount(account);

        var t6 = new Transaction();
        t6.setAmount(new BigDecimal("400"));
        t6.setDate(LocalDate.of(2022, 1, 1));
        t6.setDescription("Arroz");
        t6.setBarCode(null);
        t6.setBankSlip(false);
        t6.setTransactionType(TransactionType.EXPENSE);
        t6.setTransactionCategory(TransactionCategory.TRANSPORT);
        t6.setPaid(true);
        t6.setAccount(account);

        var t7 = new Transaction();
        t7.setAmount(new BigDecimal("800"));
        t7.setDate(LocalDate.of(2022, 1, 1));
        t7.setDescription("Arroz");
        t7.setBarCode(null);
        t7.setBankSlip(false);
        t7.setTransactionType(TransactionType.EXPENSE);
        t7.setTransactionCategory(TransactionCategory.ELECTRONIC);
        t7.setPaid(true);
        t7.setAccount(account);

        var t8 = new Transaction();
        t8.setAmount(new BigDecimal("800"));
        t8.setDate(LocalDate.of(2022, 1, 1));
        t8.setDescription("Arroz");
        t8.setBarCode(null);
        t8.setBankSlip(false);
        t8.setTransactionType(TransactionType.EXPENSE);
        t8.setTransactionCategory(TransactionCategory.ELECTRONIC);
        t8.setPaid(true);
        t8.setAccount(account);

        var t9 = new Transaction();
        t9.setAmount(new BigDecimal("1600"));
        t9.setDate(LocalDate.of(2022, 1, 1));
        t9.setDescription("Arroz");
        t9.setBarCode(null);
        t9.setBankSlip(false);
        t9.setTransactionType(TransactionType.EXPENSE);
        t9.setTransactionCategory(TransactionCategory.RESTAURANT);
        t9.setPaid(true);
        t9.setAccount(account);

        var t10 = new Transaction();
        t10.setAmount(new BigDecimal("1600"));
        t10.setDate(LocalDate.of(2022, 1, 1));
        t10.setDescription("Arroz");
        t10.setBarCode(null);
        t10.setBankSlip(false);
        t10.setTransactionType(TransactionType.EXPENSE);
        t10.setTransactionCategory(TransactionCategory.RESTAURANT);
        t10.setPaid(true);
        t10.setAccount(account);

        var t11 = new Transaction();
        t11.setAmount(new BigDecimal("1500"));
        t11.setDate(LocalDate.of(2022, 1, 1));
        t11.setDescription("Arroz");
        t11.setBarCode(null);
        t11.setBankSlip(false);
        t11.setTransactionType(TransactionType.REVENUE);
        t11.setTransactionCategory(TransactionCategory.SALARY);
        t11.setPaid(true);
        t11.setAccount(account);

        var t12 = new Transaction();
        t12.setAmount(new BigDecimal("1500"));
        t12.setDate(LocalDate.of(2022, 1, 1));
        t12.setDescription("Arroz");
        t12.setBarCode(null);
        t12.setBankSlip(false);
        t12.setTransactionType(TransactionType.REVENUE);
        t12.setTransactionCategory(TransactionCategory.SALARY);
        t12.setPaid(true);
        t12.setAccount(account);

        var t13 = new Transaction();
        ;
        t13.setAmount(new BigDecimal("600"));
        t13.setDate(LocalDate.of(2022, 1, 1));
        t13.setDescription("Arroz");
        t13.setBarCode(null);
        t13.setBankSlip(false);
        t13.setTransactionType(TransactionType.REVENUE);
        t13.setTransactionCategory(TransactionCategory.AWARD);
        t13.setPaid(true);
        t13.setAccount(account);

        var t14 = new Transaction();
        ;
        t14.setAmount(new BigDecimal("600"));
        t14.setDate(LocalDate.of(2022, 1, 1));
        t14.setDescription("Arroz");
        t14.setBarCode(null);
        t14.setBankSlip(false);
        t14.setTransactionType(TransactionType.REVENUE);
        t14.setTransactionCategory(TransactionCategory.AWARD);
        t14.setPaid(true);
        t14.setAccount(account);

        return List.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Test
    void testGetExpenseDonutsGraph() {
        var transactionEntities = getTransactionMockListGraph();
        transactionEntities.forEach(transaction -> createTransaction.execute(transaction));

        final var result = dashboard.getDonutsGraph("admin", 1, 2022, TransactionType.EXPENSE);

        assertEquals(5, result.labels().size());
        assertEquals("EDUCATION", result.labels().get(0));
        assertEquals("ELECTRONIC", result.labels().get(1));
        assertEquals("HOUSE", result.labels().get(2));
        assertEquals("RESTAURANT", result.labels().get(3));
        assertEquals("TRANSPORT", result.labels().get(4));

        assertEquals(5, result.series().size());
        assertEquals(new BigDecimal("400.00"), result.series().get(0));
        assertEquals(new BigDecimal("1600.00"), result.series().get(1));
        assertEquals(new BigDecimal("200.00"), result.series().get(2));
        assertEquals(new BigDecimal("3200.00"), result.series().get(3));
        assertEquals(new BigDecimal("800.00"), result.series().get(4));
    }

    @Test
    void testGetExpenseDonutsGraphWithFloatPoint() {
        var t1 = new Transaction();
        t1.setAmount(new BigDecimal("1.1"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.EXPENSE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(true);
        t1.setAccount(account);

        var t2 = new Transaction();
        t2.setAmount(new BigDecimal("1.3"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.EXPENSE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(true);
        t2.setAccount(account);

        var transactionEntities = List.of(t1, t2);
        transactionEntities.forEach(transaction -> createTransaction.execute(transaction));

        final var result = dashboard.getDonutsGraph("admin", 1, 2022, TransactionType.EXPENSE);

        assertEquals(1, result.labels().size());
        assertEquals("HOUSE", result.labels().get(0));

        assertEquals(1, result.series().size());
        assertEquals(new BigDecimal("2.40"), result.series().get(0));
    }

    @Test
    void testGetRevenueDonutsGraph() {
        var transactionEntities = getTransactionMockListGraph();
        transactionEntities.forEach(transaction -> createTransaction.execute(transaction));

        final var result = dashboard.getDonutsGraph("admin", 1, 2022, TransactionType.REVENUE);

        assertEquals(2, result.labels().size());
        assertEquals("AWARD", result.labels().get(0));
        assertEquals("SALARY", result.labels().get(1));

        assertEquals(2, result.series().size());
        assertEquals(new BigDecimal("1200.00"), result.series().get(0));
        assertEquals(new BigDecimal("3000.00"), result.series().get(1));
    }

    @Test
    void shouldReturn2EmptyListsIfNotPaid() {
        var t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.EXPENSE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(false);
        t1.setAccount(account);

        var t2 = new Transaction();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.REVENUE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(false);
        t2.setAccount(account);
        var transactionEntities = List.of(t1, t2);

        transactionEntities.forEach(transaction -> createTransaction.execute(transaction));

        final var result1 = dashboard.getDonutsGraph("admin", 1, 2022, TransactionType.REVENUE);
        assertEquals(0, result1.labels().size());
        assertEquals(0, result1.series().size());

        final var result2 = dashboard.getDonutsGraph("admin", 1, 2022, TransactionType.EXPENSE);
        assertEquals(0, result2.labels().size());
        assertEquals(0, result2.series().size());
    }

    @Test
    void shouldReturnEmptyListWithIncorrectData() {
        var t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.EXPENSE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(true);
        t1.setAccount(account);

        var t2 = new Transaction();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.REVENUE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(true);
        t2.setAccount(account);
        var transactionEntities = List.of(t1, t2);

        transactionEntities.forEach(transaction -> createTransaction.execute(transaction));

        final var result1 = dashboard.getDonutsGraph("admin", 2, 2022, TransactionType.REVENUE);
        assertEquals(0, result1.labels().size());
        assertEquals(0, result1.series().size());

        final var result2 = dashboard.getDonutsGraph("admin", 1, 2023, TransactionType.EXPENSE);
        assertEquals(0, result2.labels().size());
        assertEquals(0, result2.series().size());
    }
}