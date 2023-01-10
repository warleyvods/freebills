package com.freebills.usecases;
import com.freebills.domains.enums.TransactionCategory;
import java.time.LocalDate;
import com.freebills.domains.enums.TransactionType;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import com.freebills.domains.enums.AccountType;
import com.freebills.domains.enums.BankType;
import com.freebills.gateways.TransactionGateway;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateTransactionTest {

    @Autowired
    private UpdateTransaction updateTransaction;

    @Autowired
    private TransactionValidation transactionValidation;

    @Autowired
    private TransactionGateway transactionGateway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreateTransaction createTransaction;

    private static List<Account> account;

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

        final var acc02 = new Account();
        acc02.setAmount(new BigDecimal("0"));
        acc02.setDescription("Conta Nubank");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.NUBANK);
        acc02.setUser(user);
        acc02.setArchived(false);
        acc02.setDashboard(false);

        final var accs = List.of(acc01, acc02);
        account = accountsRepository.saveAll(accs);
    }

    @AfterEach
    void afterSetup() {
        accountsRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void shouldUpdateTransaction() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account.get(0));
        final var savedTransaction = createTransaction.execute(transaction);

        // changing account
        savedTransaction.setAccount(account.get(1));

        final Transaction response = updateTransaction.execute(savedTransaction);
        final List<Account> allAccounts = accountsRepository.findAll();

        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(100)));
    }

    @Test
    void shouldUpdateIncrementAmountTransaction() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account.get(0));
        final var savedTransaction = createTransaction.execute(transaction);
        savedTransaction.setAmount(new BigDecimal(100));

        final Transaction response = updateTransaction.execute(savedTransaction);
        final List<Account> allAccounts = accountsRepository.findAll();

        assertEquals(BankType.INTER, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(200)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(0)));
    }

    @Test
    void shouldUpdateDecrementAmountTransaction() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account.get(0));
        final var savedTransaction = createTransaction.execute(transaction);
        savedTransaction.setAmount(new BigDecimal(50));

        final Transaction response = updateTransaction.execute(savedTransaction);
        final List<Account> allAccounts = accountsRepository.findAll();

        assertEquals(BankType.INTER, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(50)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(0)));
    }
}