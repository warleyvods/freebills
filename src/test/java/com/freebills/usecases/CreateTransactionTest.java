package com.freebills.usecases;

import com.freebills.entities.Account;
import com.freebills.entities.Transaction;
import com.freebills.entities.User;
import com.freebills.entities.enums.AccountType;
import com.freebills.entities.enums.BankType;
import com.freebills.entities.enums.TransactionCategory;
import com.freebills.entities.enums.TransactionType;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateTransactionTest {

    @Autowired
    private CreateTransaction createTransaction;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    private static Account account;

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

    @Test
    void testCreateTransactionSuccess() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Test transaction");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account);

        Transaction transactionSaved = createTransaction.execute(transaction);

        assertNotNull(transactionSaved);
        assertEquals(transaction.getAmount(), transactionSaved.getAmount());
        assertEquals(transaction.getDate(), transactionSaved.getDate());
        assertEquals(transaction.getDescription(), transactionSaved.getDescription());
        assertEquals(transaction.getTransactionType(), transactionSaved.getTransactionType());
        assertEquals(transaction.getTransactionCategory(), transactionSaved.getTransactionCategory());
        assertEquals(transaction.isPaid(), transactionSaved.isPaid());
        assertEquals(account, transactionSaved.getAccount());
    }

    @Test
    void testCreateTransactionWithNullAccount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Test transaction");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(null);

        assertThrows(NullPointerException.class, () -> createTransaction.execute(transaction));
    }

    @Test
    void testCreateTransactionWithNullDate() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(null);
        transaction.setDescription("Test transaction");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account);

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transaction));
    }

    @Test
    void testCreateTransactionWithNullDescription() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription(null);
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account);

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transaction));
    }

    @Test
    void testCreateTransactionWithNullTransactionType() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Test transaction");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(null);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account);

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transaction));
    }

    @Test
    void testCreateTransactionWithNullTransactionCategory() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Test transaction");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(null);
        transaction.setPaid(true);
        transaction.setAccount(account);

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transaction));
    }

    @Test
    void testCreateTransactionWithLargeDescription() {
        String description = "This is a very long description that exceeds the maximum length allowed by the database column for the description field." +
                " It should trigger a constraint violation exception when attempting to save the transaction.";
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription(description);
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.REVENUE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account);

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transaction));
    }
}