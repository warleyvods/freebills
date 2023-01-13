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
import org.junit.jupiter.api.Disabled;
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
    void shouldUpdateTransactionToAnotherAccountREVENUE() {
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

        // before transaction
        final List<Account> beforeTransaction = accountsRepository.findAll();
        assertEquals(0, beforeTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.INTER,  beforeTransaction.get(0).getBankType());

        assertEquals(0, beforeTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.NUBANK,  beforeTransaction.get(1).getBankType());

        // save transaction
        final var savedTransaction = createTransaction.execute(transaction);

        // after transaction
        final List<Account> afterTransaction = accountsRepository.findAll();
        assertEquals(0, afterTransaction.get(0).getAmount().compareTo(new BigDecimal(100)));
        assertEquals(BankType.INTER,  afterTransaction.get(0).getBankType());

        assertEquals(0, afterTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.NUBANK,  afterTransaction.get(1).getBankType());

        // changing account
        savedTransaction.setFromAccount(account.get(0).getId());
        savedTransaction.setToAccount(account.get(1).getId());
        savedTransaction.setTransactionChange(true);
        savedTransaction.setAccount(account.get(1));

        final Transaction response = updateTransaction.execute(savedTransaction);
        final List<Account> allAccounts = accountsRepository.findAll();

        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(100)));
    }

    @Test
    void shouldUpdateTransactionToAnotherAccountEXPENSE() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.EXPENSE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account.get(0));

        // before transaction
        final List<Account> beforeTransaction = accountsRepository.findAll();
        assertEquals(0, beforeTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.INTER,  beforeTransaction.get(0).getBankType());

        assertEquals(0, beforeTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.NUBANK,  beforeTransaction.get(1).getBankType());

        // save transaction
        final var savedTransaction = createTransaction.execute(transaction);

        // after transaction
        final List<Account> afterTransaction = accountsRepository.findAll();
        assertEquals(0, afterTransaction.get(0).getAmount().compareTo(new BigDecimal(-100)));
        assertEquals(BankType.INTER,  afterTransaction.get(0).getBankType());

        assertEquals(0, afterTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.NUBANK,  afterTransaction.get(1).getBankType());

        // changing account
        savedTransaction.setFromAccount(account.get(0).getId());
        savedTransaction.setToAccount(account.get(1).getId());
        savedTransaction.setTransactionChange(true);
        savedTransaction.setAccount(account.get(1));

        final Transaction response = updateTransaction.execute(savedTransaction);
        final List<Account> allAccounts = accountsRepository.findAll();

        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(-100)));
    }

    @Test
    void shouldUpdateTransactionToAnotherAccountEXPENSENotPaid() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.EXPENSE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(false);
        transaction.setAccount(account.get(0));

        // before transaction
        final List<Account> beforeTransaction = accountsRepository.findAll();
        assertEquals(0, beforeTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.INTER,  beforeTransaction.get(0).getBankType());

        assertEquals(0, beforeTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.NUBANK,  beforeTransaction.get(1).getBankType());

        // save transaction
        final var savedTransaction = createTransaction.execute(transaction);

        // after transaction
        final List<Account> afterTransaction = accountsRepository.findAll();
        assertEquals(0, afterTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.INTER,  afterTransaction.get(0).getBankType());

        assertEquals(0, afterTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(BankType.NUBANK,  afterTransaction.get(1).getBankType());

        // changing account
        savedTransaction.setFromAccount(account.get(0).getId());
        savedTransaction.setToAccount(account.get(1).getId());
        savedTransaction.setTransactionChange(true);
        savedTransaction.setAccount(account.get(1));

        final Transaction response = updateTransaction.execute(savedTransaction);
        final List<Account> allAccounts = accountsRepository.findAll();

        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(0)));
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

    @Test
    void shouldDecrementAndIncrementValueFromAccountChangingPaidStatusEXPENSE() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.EXPENSE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account.get(0));

        final List<Account> beforeAccounts = accountsRepository.findAll();
        assertEquals(0, beforeAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));

        final var savedTransaction = createTransaction.execute(transaction);

        final List<Account> afterAccounts = accountsRepository.findAll();
        assertEquals(0, afterAccounts.get(0).getAmount().compareTo(new BigDecimal(-100)));

        // change status
        savedTransaction.setPaid(false);

        // act
        final Transaction response = updateTransaction.execute(savedTransaction);

        // asserts
        final List<Account> allAccounts = accountsRepository.findAll();
        assertEquals(BankType.INTER, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(0)));
    }

    @Test
    void shouldDecrementAndIncrementValueFromAccountChangingPaidStatusREVENUE() {
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

        final List<Account> beforeAccounts = accountsRepository.findAll();
        assertEquals(0, beforeAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));

        final var savedTransaction = createTransaction.execute(transaction);

        final List<Account> afterAccounts = accountsRepository.findAll();
        assertEquals(0, afterAccounts.get(0).getAmount().compareTo(new BigDecimal(100)));

        // change status
        savedTransaction.setPaid(false);

        // act
        final Transaction response = updateTransaction.execute(savedTransaction);

        // asserts
        final List<Account> allAccounts = accountsRepository.findAll();
        assertEquals(BankType.INTER, response.getAccount().getBankType());
        assertEquals(0, allAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));
        assertEquals(0, allAccounts.get(1).getAmount().compareTo(new BigDecimal(0)));
    }

    @Test
    void shouldDecrementAndIncrementValueFromAccountChangingPaidStatus() {
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

        final List<Account> beforeAccounts = accountsRepository.findAll();
        assertEquals(0, beforeAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));

        // criei ja paga
        final var savedTransaction = createTransaction.execute(transaction);

        final List<Account> afterAccounts = accountsRepository.findAll();
        assertEquals(0, afterAccounts.get(0).getAmount().compareTo(new BigDecimal(100)));

        // altero o valor para menor
        savedTransaction.setAmount(BigDecimal.valueOf(50));
        final Transaction savedTransaction01 = updateTransaction.execute(savedTransaction);

        final List<Account> alterValue = accountsRepository.findAll();
        assertEquals(0, alterValue.get(0).getAmount().compareTo(new BigDecimal(50)));

        // altero de volta pra 100 e salvo de novo
        savedTransaction01.setAmount(BigDecimal.valueOf(100));
        final Transaction savedTransaction02 = updateTransaction.execute(savedTransaction01);

        final List<Account> alterValue01 = accountsRepository.findAll();
        assertEquals(0, alterValue01.get(0).getAmount().compareTo(new BigDecimal(100)));
    }

    //TODO arrumar o mais rapido possivel
    @Disabled
    @Test
    void shouldDecreaseAmountIfPaidExpenseToPaidRevenue() {
        final var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Arroz");
        transaction.setBarCode(null);
        transaction.setBankSlip(false);
        transaction.setTransactionType(TransactionType.EXPENSE);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account.get(0));

        // conta deve não ter valor algum R$ 0.00
        final List<Account> beforeAccounts = accountsRepository.findAll();
        assertEquals(0, beforeAccounts.get(0).getAmount().compareTo(new BigDecimal(0)));

        // criei ja paga (deve reduzir o valor da conta 0 pra -100)
        final var savedTransaction = createTransaction.execute(transaction);

        final List<Account> afterAccounts = accountsRepository.findAll();
        assertEquals(0, afterAccounts.get(0).getAmount().compareTo(new BigDecimal(-100)));

        // altero o tipo de EXPENSE pra REVENUE e salvo
        savedTransaction.setTransactionType(TransactionType.REVENUE);
        final Transaction savedTransaction01 = updateTransaction.execute(savedTransaction);

        // deve reduzir o valor da conta 0 para R$ 0.00 e adicionar 100 reais positivo pra conta.
        final List<Account> alterValue = accountsRepository.findAll();
        assertEquals(0, alterValue.get(0).getAmount().compareTo(new BigDecimal(100)));
    }
}