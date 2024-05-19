package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.mapper.AccountGatewayMapper;
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

    @Autowired
    private AccountGatewayMapper accountGatewayMapper;

    private static AccountEntity accountEntity;

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

        accountEntity = accountsRepository.save(acc01);
    }

    @Test
    void testCreateTransactionSuccess() {
        var transactionEntity = new Transaction();
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setDescription("Test transaction");
        transactionEntity.setBarCode(null);
        transactionEntity.setBankSlip(false);
        transactionEntity.setTransactionType(TransactionType.REVENUE);
        transactionEntity.setTransactionCategory(TransactionCategory.HOUSE);
        transactionEntity.setPaid(true);
        transactionEntity.setAccount(accountGatewayMapper.toDomain(accountEntity));

        var transactionSaved = createTransaction.execute(transactionEntity);

        assertNotNull(transactionSaved);
        assertEquals(transactionEntity.getAmount(), transactionSaved.getAmount());
        assertEquals(transactionEntity.getDate(), transactionSaved.getDate());
        assertEquals(transactionEntity.getDescription(), transactionSaved.getDescription());
        assertEquals(transactionEntity.getTransactionType(), transactionSaved.getTransactionType());
        assertEquals(transactionEntity.getTransactionCategory(), transactionSaved.getTransactionCategory());
        assertEquals(transactionEntity.getPaid(), transactionSaved.getPaid());
        assertEquals(transactionEntity.getAccount(), transactionSaved.getAccount());
    }

    @Test
    void testCreateTransactionWithNullAccount() {
        var transactionEntity = new Transaction();
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setDescription("Test transaction");
        transactionEntity.setBarCode(null);
        transactionEntity.setBankSlip(false);
        transactionEntity.setTransactionType(TransactionType.REVENUE);
        transactionEntity.setTransactionCategory(TransactionCategory.HOUSE);
        transactionEntity.setPaid(true);
        transactionEntity.setAccount(null);

        assertThrows(NullPointerException.class, () -> createTransaction.execute(transactionEntity));
    }

    @Test
    void testCreateTransactionWithNullDate() {
        var transactionEntity = new Transaction();
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setDate(null);
        transactionEntity.setDescription("Test transaction");
        transactionEntity.setBarCode(null);
        transactionEntity.setBankSlip(false);
        transactionEntity.setTransactionType(TransactionType.REVENUE);
        transactionEntity.setTransactionCategory(TransactionCategory.HOUSE);
        transactionEntity.setPaid(true);
        transactionEntity.setAccount(accountGatewayMapper.toDomain(accountEntity));

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transactionEntity));
    }

    @Test
    void testCreateTransactionWithNullDescription() {
        var transactionEntity = new Transaction();
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setDescription(null);
        transactionEntity.setBarCode(null);
        transactionEntity.setBankSlip(false);
        transactionEntity.setTransactionType(TransactionType.REVENUE);
        transactionEntity.setTransactionCategory(TransactionCategory.HOUSE);
        transactionEntity.setPaid(true);
        transactionEntity.setAccount(accountGatewayMapper.toDomain(accountEntity));

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transactionEntity));
    }

    @Test
    void testCreateTransactionWithLargeDescription() {
        String description = "This is a very long description that exceeds the maximum length allowed by the database column for the description field." +
                " It should trigger a constraint violation exception when attempting to save the transaction." +
                        " It should trigger a constraint violation exception when attempting to save the transaction." +
                        " It should trigger a constraint violation exception when attempting to save the transaction." +
                        " It should trigger a constraint violation exception when attempting to save the transaction.";
                var transactionEntity = new Transaction();
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setDescription(description);
        transactionEntity.setBarCode(null);
        transactionEntity.setBankSlip(false);
        transactionEntity.setTransactionType(TransactionType.REVENUE);
        transactionEntity.setTransactionCategory(TransactionCategory.HOUSE);
        transactionEntity.setPaid(true);
        transactionEntity.setAccount(accountGatewayMapper.toDomain(accountEntity));

        assertThrows(ConstraintViolationException.class, () -> createTransaction.execute(transactionEntity));
    }
}
