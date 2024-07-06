package com.freebills.repositories;

import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.entities.Source;
import com.freebills.gateways.entities.TransferEntity;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.entities.enums.BankType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.freebills.gateways.entities.enums.AccountType.CHECKING_ACCOUNT;
import static com.freebills.gateways.entities.enums.BankType.CAIXA;
import static com.freebills.gateways.entities.enums.BankType.INTER;

@ActiveProfiles("tc")
@SpringBootTest
class TransferRepositoryTest {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        transferRepository.deleteAll();
        accountsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByTransferIdAndUser() {
        UserEntity user = createUserEntity();
        final UserEntity savedUser = userRepository.save(user);

        AccountEntity account1 = createAccountEntity(savedUser, "BANCO INTER", INTER);
        AccountEntity account2 = createAccountEntity(savedUser, "BANCO CAIXA", CAIXA);
        accountsRepository.saveAll(List.of(account1, account2));

        TransferEntity transferEntity = createTransferEntity(account1, account2, "DESCRIPTION");
        final TransferEntity savedTranfer = transferRepository.save(transferEntity);

        final var found = transferRepository.findByTransferIdAndUser(savedTranfer.getId(), "test0001");

        Assertions.assertEquals(transferEntity.getDescription(), found.get().getDescription());
    }

    @Test
    void findAllByUsername() {
        UserEntity user = createUserEntity();
        final UserEntity savedUser = userRepository.save(user);

        AccountEntity account1 = createAccountEntity(savedUser, "BANCO INTER", INTER);
        AccountEntity account2 = createAccountEntity(savedUser, "BANCO CAIXA", CAIXA);
        accountsRepository.saveAll(List.of(account1, account2));

        TransferEntity transferEntity = createTransferEntity(account1, account2, "DESCRIPTION");
        transferRepository.save(transferEntity);

        final var found = transferRepository.findAllByUsername("test0001", null, null, null);

        Assertions.assertEquals(transferEntity.getDescription(), found.getContent().getFirst().getDescription());
    }

    @Test
    void findAllByUsernameWithYearAndMonth() {
        UserEntity user = createUserEntity();
        final UserEntity savedUser = userRepository.save(user);

        AccountEntity account1 = createAccountEntity(savedUser, "BANCO INTER", INTER);
        AccountEntity account2 = createAccountEntity(savedUser, "BANCO CAIXA", CAIXA);
        accountsRepository.saveAll(List.of(account1, account2));

        TransferEntity transferEntity1 = createTransferEntity(account1, account2, "DESCRIPTION 1", LocalDate.of(2024, 4, 10));
        TransferEntity transferEntity2 = createTransferEntity(account1, account2, "DESCRIPTION 2", LocalDate.of(2025, 5, 10));
        transferRepository.saveAll(List.of(transferEntity1, transferEntity2));

        final var found1 = transferRepository.findAllByUsername("test0001", 2024, 4, null);
        final var found2 = transferRepository.findAllByUsername("test0001", 2025, 5, null);
        final var found3 = transferRepository.findAllByUsername("test0001", 2026, 1, null);
        final var found4 = transferRepository.findAllByUsername("test0002", null, null, null);

        Assertions.assertEquals(transferEntity1.getDescription(), found1.getContent().getFirst().getDescription());
        Assertions.assertEquals(transferEntity2.getDescription(), found2.getContent().getFirst().getDescription());
        Assertions.assertTrue(found3.getContent().isEmpty());
        Assertions.assertTrue(found4.getContent().isEmpty());
    }

    private UserEntity createUserEntity() {
        UserEntity user = new UserEntity();
        user.setName("User Test");
        user.setLogin("test0001");
        user.setEmail("test0001@test.com");
        user.setPassword("test1@3");
        user.setAdmin(true);
        user.setActive(true);
        user.setSource(Source.LOCAL);
        user.setAccounts(List.of());
        user.setCategories(List.of());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setLastAccess(LocalDateTime.now());
        return user;
    }

    private AccountEntity createAccountEntity(UserEntity user, String description, BankType bankType) {
        AccountEntity account = new AccountEntity();
        account.setDescription(description);
        account.setAccountType(CHECKING_ACCOUNT);
        account.setBankType(bankType);
        account.setUser(user);
        account.setArchived(false);
        account.setDashboard(false);
        account.setTransactions(List.of());
        account.setCreatedAt(LocalDateTime.now());
        return account;
    }

    private TransferEntity createTransferEntity(AccountEntity from, AccountEntity to, String description) {
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setObservation("OBSERVATION");
        transferEntity.setAmount(new BigDecimal("1000"));
        transferEntity.setDate(LocalDate.now());
        transferEntity.setDescription(description);
        transferEntity.setTransferCategory("");
        transferEntity.setFrom(from);
        transferEntity.setTo(to);
        transferEntity.setUpdatedAt(LocalDateTime.now());
        transferEntity.setCreatedAt(LocalDateTime.now());
        return transferEntity;
    }

    private TransferEntity createTransferEntity(AccountEntity from, AccountEntity to, String description, LocalDate date) {
        TransferEntity transferEntity = createTransferEntity(from, to, description);
        transferEntity.setDate(date);
        return transferEntity;
    }
}
