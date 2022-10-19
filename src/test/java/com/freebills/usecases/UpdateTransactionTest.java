package com.freebills.usecases;

import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import com.freebills.domains.enums.AccountType;
import com.freebills.domains.enums.BankType;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.UserGateway;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdateTransactionTest {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountGateway accountGateway;

    @Autowired
    private UpdateTransaction updateTransaction;

    @Autowired
    private TransactionGateway transactionGateway;

    @Autowired
    private UserGateway userGateway;

    private User user;

//    @BeforeEach
//    void before() {
//        User user = new User();
//        user.setName("Test");
//        user.setLogin("test");
//        user.setEmail("test@test.com");
//        user.setPassword("123");
//        user.setAdmin(false);
//        user.setActive(true);
//        user.setCreatedAt(LocalDate.now());
//        user.setAccounts(List.of());
//        this.user = userGateway.save(user);
//
//        transactionRepository.deleteAll();
//        accountsRepository.deleteAll();
//    }
//
//    @AfterEach
//    void after() {
//
//    }


    @Test
    void shouldSumNewValueTransactionWhenTransactionStatusChangedToPaid() {
        User user = new User();
        user.setName("Test");
        user.setLogin(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("123");
        user.setAdmin(false);
        user.setActive(true);
        user.setCreatedAt(LocalDate.now());
        user.setAccounts(List.of());
        final User savedUser = userGateway.save(user);

        Account account = new Account();
        account.setAmount(new BigDecimal("0"));
        account.setDescription("Conta Inter");
        account.setAccountType(AccountType.CHECKING_ACCOUNT);
        account.setBankType(BankType.INTER);
        account.setUser(savedUser);
        account.setArchived(false);
        account.setDashboard(false);
        final Account savedAccount = accountGateway.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(1000));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Transação teste");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(false);
        transaction.setAccount(savedAccount);
        final Transaction transactionBeforeUpdate = transactionGateway.save(transaction);

        transactionBeforeUpdate.setPaid(true);

        final Transaction updatedTransaction = updateTransaction.update(transactionBeforeUpdate);
        final Account updatedAccount = accountGateway.findById(savedAccount.getId());

        assertTrue(updatedTransaction.isPaid());
        assertTrue(new BigDecimal(1000).compareTo(updatedAccount.getAmount()) == 0);

        //ROLLBACK STATUS
        updatedTransaction.setPaid(false);
        final Transaction updatedTransactionRollback = updateTransaction.update(updatedTransaction);
        final Account updatedAccount2 = accountGateway.findById(savedAccount.getId());

        assertFalse(updatedTransactionRollback.isPaid());
        assertTrue(new BigDecimal(0).compareTo(updatedAccount2.getAmount()) == 0);
    }

    @Disabled
    @Test
    void shouldNotSumNewValueTransactionWhenTransactionStatusChangedToPaid() {
        User user = new User();
        user.setName("Test");
        user.setLogin(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("123");
        user.setAdmin(false);
        user.setActive(true);
        user.setCreatedAt(LocalDate.now());
        user.setAccounts(List.of());
        final User savedUser = userGateway.save(user);

        Account account = new Account();
        account.setAmount(new BigDecimal("1000"));
        account.setDescription("Conta Inter");
        account.setAccountType(AccountType.CHECKING_ACCOUNT);
        account.setBankType(BankType.INTER);
        account.setUser(savedUser);
        account.setArchived(false);
        account.setDashboard(false);
        final Account savedAccount = accountGateway.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(500));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Transação teste");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(savedAccount);
        final Transaction transactionBeforeUpdate = transactionGateway.save(transaction);

        transactionBeforeUpdate.setPaid(false);

        final Transaction updatedTransaction = updateTransaction.update(transactionBeforeUpdate);
        final Account updatedAccount = accountGateway.findById(savedAccount.getId());

        assertTrue(updatedTransaction.isPaid());
        assertTrue(new BigDecimal(500).compareTo(updatedAccount.getAmount()) == 0);
    }
}