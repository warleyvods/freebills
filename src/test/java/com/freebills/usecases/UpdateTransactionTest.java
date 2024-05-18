//package com.freebills.usecases;
//
//import com.freebills.domain.Account;
//import com.freebills.domain.Transaction;
//import com.freebills.gateways.TransactionGateway;
//import com.freebills.gateways.entities.AccountEntity;
//import com.freebills.gateways.entities.UserEntity;
//import com.freebills.gateways.entities.enums.AccountType;
//import com.freebills.gateways.entities.enums.BankType;
//import com.freebills.gateways.entities.enums.TransactionCategory;
//import com.freebills.gateways.entities.enums.TransactionType;
//import com.freebills.gateways.mapper.AccountGatewayMapper;
//import com.freebills.repositories.AccountsRepository;
//import com.freebills.repositories.TransactionRepository;
//import com.freebills.repositories.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UpdateTransactionTest {
//
//    @Autowired
//    private UpdateTransaction updateTransaction;
//
//    @Autowired
//    private TransactionValidation transactionValidation;
//
//    @Autowired
//    private TransactionGateway transactionGateway;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AccountsRepository accountsRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private CreateTransaction createTransaction;
//
//    @Autowired
//    private AccountGatewayMapper accountGatewayMapper;
//
//    private static List<Account> accounts;
//
//    @BeforeEach
//    void beforeSetup() {
//        UserEntity userEntity = userRepository.findById(1L).orElse(null);
//
//        final var acc01 = new AccountEntity();
//        acc01.setAmount(new BigDecimal("0"));
//        acc01.setDescription("Conta Inter");
//        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
//        acc01.setBankType(BankType.INTER);
//        acc01.setUser(userEntity);
//        acc01.setArchived(false);
//        acc01.setDashboard(false);
//
//        final var acc02 = new AccountEntity();
//        acc02.setAmount(new BigDecimal("0"));
//        acc02.setDescription("Conta Nubank");
//        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
//        acc02.setBankType(BankType.NUBANK);
//        acc02.setUser(userEntity);
//        acc02.setArchived(false);
//        acc02.setDashboard(false);
//
//        final var accs = List.of(acc01, acc02);
//        accounts = accountsRepository.saveAll(accs).stream().map(accountGatewayMapper::toDomain).toList();
//    }
//
//    @AfterEach
//    void afterSetup() {
//        accountsRepository.deleteAll();
//        transactionRepository.deleteAll();
//    }
//
//    @Test
//    void shouldUpdateTransactionToAnotherAccountREVENUE() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        // before transaction
//        final List<AccountEntity> beforeTransaction = accountsRepository.findAll();
//        assertEquals(0, beforeTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.INTER,  beforeTransaction.get(0).getBankType());
//
//        assertEquals(0, beforeTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.NUBANK,  beforeTransaction.get(1).getBankType());
//
//        // save transaction
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        // after transaction
//        final List<AccountEntity> afterTransaction = accountsRepository.findAll();
//        assertEquals(0, afterTransaction.get(0).getAmount().compareTo(new BigDecimal(100)));
//        assertEquals(BankType.INTER,  afterTransaction.get(0).getBankType());
//
//        assertEquals(0, afterTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.NUBANK,  afterTransaction.get(1).getBankType());
//
//        // changing account
//        savedTransaction.setFromAccount(accounts.get(0).getId());
//        savedTransaction.setToAccount(accounts.get(1).getId());
//        savedTransaction.setTransactionChange(true);
//        savedTransaction.setAccount(accounts.get(1));
//
//        final var response = updateTransaction.execute(savedTransaction);
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//
//        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(100)));
//    }
//
//    @Test
//    void shouldUpdateTransactionToAnotherAccountEXPENSE() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.EXPENSE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        // before transaction
//        final List<AccountEntity> beforeTransaction = accountsRepository.findAll();
//        assertEquals(0, beforeTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.INTER,  beforeTransaction.get(0).getBankType());
//
//        assertEquals(0, beforeTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.NUBANK,  beforeTransaction.get(1).getBankType());
//
//        // save transaction
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        // after transaction
//        final List<AccountEntity> afterTransaction = accountsRepository.findAll();
//        assertEquals(0, afterTransaction.get(0).getAmount().compareTo(new BigDecimal(-100)));
//        assertEquals(BankType.INTER,  afterTransaction.get(0).getBankType());
//
//        assertEquals(0, afterTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.NUBANK,  afterTransaction.get(1).getBankType());
//
//        // changing account
//        savedTransaction.setFromAccount(accounts.get(0).getId());
//        savedTransaction.setToAccount(accounts.get(1).getId());
//        savedTransaction.setTransactionChange(true);
//        savedTransaction.setAccount(accounts.get(1));
//
//        final var response = updateTransaction.execute(savedTransaction);
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//
//        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(-100)));
//    }
//
//    @Test
//    void shouldUpdateTransactionToAnotherAccountEXPENSENotPaid() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.EXPENSE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(false);
//        transaction.setAccount(accounts.get(0));
//
//        // before transaction
//        final List<AccountEntity> beforeTransaction = accountsRepository.findAll();
//        assertEquals(0, beforeTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.INTER,  beforeTransaction.get(0).getBankType());
//
//        assertEquals(0, beforeTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.NUBANK,  beforeTransaction.get(1).getBankType());
//
//        // save transaction
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        // after transaction
//        final List<AccountEntity> afterTransaction = accountsRepository.findAll();
//        assertEquals(0, afterTransaction.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.INTER,  afterTransaction.get(0).getBankType());
//
//        assertEquals(0, afterTransaction.get(1).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(BankType.NUBANK,  afterTransaction.get(1).getBankType());
//
//        // changing account
//        savedTransaction.setFromAccount(accounts.get(0).getId());
//        savedTransaction.setToAccount(accounts.get(1).getId());
//        savedTransaction.setTransactionChange(true);
//        savedTransaction.setAccount(accounts.get(1));
//
//        final var response = updateTransaction.execute(savedTransaction);
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//
//        assertEquals(BankType.NUBANK, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(0)));
//    }
//
//    @Test
//    void shouldUpdateIncrementAmountTransaction() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> allBefore = accountsRepository.findAll();
//        assertEquals(0, allBefore.get(0).getAmount().compareTo(new BigDecimal(100)));
//        assertEquals(0, allBefore.get(1).getAmount().compareTo(new BigDecimal(0)));
//
//        savedTransaction.setAmount(new BigDecimal(150));
//
//        final var response = updateTransaction.execute(savedTransaction);
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//
//        assertEquals(BankType.INTER, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(150)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(0)));
//    }
//
//    @Test
//    void shouldUpdateDecrementAmountTransaction() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//        final var savedTransaction = createTransaction.execute(transaction);
//        savedTransaction.setAmount(new BigDecimal(50));
//
//        final var response = updateTransaction.execute(savedTransaction);
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//
//        assertEquals(BankType.INTER, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(50)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(0)));
//    }
//
//    @Test
//    void shouldDecrementAndIncrementValueFromAccountChangingPaidStatusEXPENSE() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.EXPENSE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        final List<AccountEntity> beforeAccountEntities = accountsRepository.findAll();
//        assertEquals(0, beforeAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> afterAccountEntities = accountsRepository.findAll();
//        assertEquals(0, afterAccountEntities.get(0).getAmount().compareTo(new BigDecimal(-100)));
//
//        // change status
//        savedTransaction.setPaid(false);
//
//        // act
//        final var response = updateTransaction.execute(savedTransaction);
//
//        // asserts
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//        assertEquals(BankType.INTER, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(0)));
//    }
//
//    @Test
//    void shouldDecrementAndIncrementValueFromAccountChangingPaidStatusREVENUE() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        final List<AccountEntity> beforeAccountEntities = accountsRepository.findAll();
//        assertEquals(0, beforeAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> afterAccountEntities = accountsRepository.findAll();
//        assertEquals(0, afterAccountEntities.get(0).getAmount().compareTo(new BigDecimal(100)));
//
//        // change status
//        savedTransaction.setPaid(false);
//
//        // act
//        final var response = updateTransaction.execute(savedTransaction);
//
//        // asserts
//        final List<AccountEntity> allAccountEntities = accountsRepository.findAll();
//        assertEquals(BankType.INTER, response.getAccount().getBankType());
//        assertEquals(0, allAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//        assertEquals(0, allAccountEntities.get(1).getAmount().compareTo(new BigDecimal(0)));
//    }
//
//    @Test
//    void shouldDecrementAndIncrementValueFromAccountChangingPaidStatus() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        final List<AccountEntity> beforeAccountEntities = accountsRepository.findAll();
//        assertEquals(0, beforeAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//
//        // criei ja paga
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> afterAccountEntities = accountsRepository.findAll();
//        assertEquals(0, afterAccountEntities.get(0).getAmount().compareTo(new BigDecimal(100)));
//
//        // altero o valor para menor
//        savedTransaction.setAmount(BigDecimal.valueOf(50));
//        final var savedTransaction01Entity = updateTransaction.execute(savedTransaction);
//
//        final List<AccountEntity> alterValue = accountsRepository.findAll();
//        assertEquals(0, alterValue.get(0).getAmount().compareTo(new BigDecimal(50)));
//
//        // altero de volta pra 100 e salvo de novo
//        savedTransaction01Entity.setAmount(BigDecimal.valueOf(100));
//        final var savedTransaction02Entity = updateTransaction.execute(savedTransaction01Entity);
//
//        final List<AccountEntity> alterValue01 = accountsRepository.findAll();
//        assertEquals(0, alterValue01.get(0).getAmount().compareTo(new BigDecimal(100)));
//    }
//
//    @Test
//    void shouldDecreaseAmountIfPaidExpenseToPaidRevenue() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.EXPENSE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        // conta deve não ter valor algum R$ 0.00
//        final List<AccountEntity> beforeAccountEntities = accountsRepository.findAll();
//        assertEquals(0, beforeAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//
//        // criei ja paga (deve reduzir o valor da conta 0 pra -100)
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> afterAccountEntities = accountsRepository.findAll();
//        assertEquals(0, afterAccountEntities.get(0).getAmount().compareTo(new BigDecimal(-100)));
//
//        // altero o tipo de EXPENSE pra REVENUE e salvo
//        savedTransaction.setTransactionType(TransactionType.REVENUE);
//        final var savedTransaction01Entity = updateTransaction.execute(savedTransaction);
//
//        // deve reduzir o valor da conta 0 para R$ 0.00 e adicionar 100 reais positivo pra conta.
//        final List<AccountEntity> alterValue = accountsRepository.findAll();
//        assertEquals(0, alterValue.get(0).getAmount().compareTo(new BigDecimal(100)));
//    }
//
//    @Test
//    void shouldIncrementAmountIfPaidExpenseToPaidRevenue() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        // conta deve não ter valor algum R$ 0.00
//        final List<AccountEntity> beforeAccountEntities = accountsRepository.findAll();
//        assertEquals(0, beforeAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//
//        // criei ja paga (deve reduzir o valor da conta 0 pra +100)
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> afterAccountEntities = accountsRepository.findAll();
//        assertEquals(0, afterAccountEntities.get(0).getAmount().compareTo(new BigDecimal(100)));
//
//        // altero o tipo de REVENUE pra EXPENSE e salvo
//        savedTransaction.setTransactionType(TransactionType.EXPENSE);
//        final var savedTransaction01Entity = updateTransaction.execute(savedTransaction);
//
//        // deve reduzir o valor da conta 0 para R$ 100 e adicionar 0.00 reais pra conta.
//        final List<AccountEntity> alterValue = accountsRepository.findAll();
//        assertEquals(0, alterValue.get(0).getAmount().compareTo(new BigDecimal(-100)));
//    }
//
//    @Test
//    void shouldNotCreateANewTransactionIfNotChangeNothingFromTransaction() {
//        final var transaction = new Transaction();
//        transaction.setAmount(new BigDecimal("100"));
//        transaction.setDate(LocalDate.now());
//        transaction.setDescription("Arroz");
//        transaction.setBarCode(null);
//        transaction.setBankSlip(false);
//        transaction.setTransactionType(TransactionType.REVENUE);
//        transaction.setTransactionCategory(TransactionCategory.HOUSE);
//        transaction.setPaid(true);
//        transaction.setAccount(accounts.get(0));
//
//        // conta deve não ter valor algum R$ 0.00
//        final List<AccountEntity> beforeAccountEntities = accountsRepository.findAll();
//        assertEquals(0, beforeAccountEntities.get(0).getAmount().compareTo(new BigDecimal(0)));
//
//        // criei ja paga (deve aumentar o valor da conta 0 pra +100)
//        final var savedTransaction = createTransaction.execute(transaction);
//
//        final List<AccountEntity> afterAccountEntities = accountsRepository.findAll();
//        assertEquals(0, afterAccountEntities.get(0).getAmount().compareTo(new BigDecimal(100)));
//
//        //SALVA DE NOVO
//        updateTransaction.execute(savedTransaction);
//
//        final List<AccountEntity> afterAccounts2 = accountsRepository.findAll();
//        assertEquals(0, afterAccounts2.get(0).getAmount().compareTo(new BigDecimal(100)));
//    }
//}
