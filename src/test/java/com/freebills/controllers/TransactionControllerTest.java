package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.LoginRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequesDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.gateways.entities.Account;
import com.freebills.gateways.entities.Transaction;
import com.freebills.gateways.entities.User;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountGateway accountGateway;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static Account account;
    private static String token;

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

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);
    }

    @AfterEach
    void afterSetup() {
        transactionRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    private List<Transaction> transactionMockList() {
        Transaction t1 = new Transaction();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.REVENUE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(false);
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
        t3.setTransactionType(TransactionType.REVENUE);
        t3.setTransactionCategory(TransactionCategory.HOUSE);
        t3.setPaid(false);
        t3.setAccount(account);

        Transaction t4 = new Transaction();
        t4.setAmount(new BigDecimal("100"));
        t4.setDate(LocalDate.of(2022, 1, 1));
        t4.setDescription("Arroz");
        t4.setBarCode(null);
        t4.setBankSlip(false);
        t4.setTransactionType(TransactionType.REVENUE);
        t4.setTransactionCategory(TransactionCategory.HOUSE);
        t4.setPaid(false);
        t4.setAccount(account);

        return List.of(t1, t2, t3, t4);
    }

    @Test
    void shouldSaveOneTransactionRevenue() {
        final var transaction = new TransactionPostRequestDTO(
                account.getId(),
                100.00D,
                true,
                "Arroz",
                LocalDate.of(2022, 10, 10),
                TransactionType.REVENUE.name(),
                TransactionCategory.RESTAURANT.name(),
                null,
                false
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(transaction, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
                HttpMethod.POST,
                request,
                TransactionResponseDTO.class);

        final Account acc = accountGateway.findById(account.getId());

        //Account
        assertEquals(0, new BigDecimal(100).compareTo(acc.getAmount()));

        assertEquals(HttpStatus.CREATED.value(), transactionResponse.getStatusCode().value());
        assertNotNull(transactionResponse.getBody());
    }

    @Test
    void shouldSaveOneTransactionRevenueNotPaid() {
        final var transaction = new TransactionPostRequestDTO(
                account.getId(),
                100.00D,
                false,
                "Arroz",
                LocalDate.of(2022, 10, 10),
                TransactionType.REVENUE.name(),
                TransactionCategory.RESTAURANT.name(),
                null,
                false
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(transaction, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
                HttpMethod.POST,
                request,
                TransactionResponseDTO.class);

        final Account acc = accountGateway.findById(account.getId());

        //Account
        assertEquals(0, new BigDecimal(0).compareTo(acc.getAmount()));

        assertEquals(HttpStatus.CREATED.value(), transactionResponse.getStatusCode().value());
        assertNotNull(transactionResponse.getBody());
    }

    @Test
    void shouldSaveOneTransactionExpense() {
        final var transaction = new TransactionPostRequestDTO(
                account.getId(),
                100.00D,
                true,
                "Arroz",
                LocalDate.of(2022, 10, 10),
                TransactionType.EXPENSE.name(),
                TransactionCategory.RESTAURANT.name(),
                null,
                false
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(transaction, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
                HttpMethod.POST,
                request,
                TransactionResponseDTO.class);

        final Account acc = accountGateway.findById(account.getId());

        //Account
        assertEquals(0, new BigDecimal(-100).compareTo(acc.getAmount()));

        assertEquals(HttpStatus.CREATED.value(), transactionResponse.getStatusCode().value());
        assertNotNull(transactionResponse.getBody());
    }

    @Test
    void shouldFindTransactionById() {
        final List<Transaction> transactions = transactionRepository.saveAll(transactionMockList());
        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(null, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions/" + transactions.get(0).getId(),
                HttpMethod.GET,
                request,
                TransactionResponseDTO.class);

        assertEquals(HttpStatus.OK.value(), transactionResponse.getStatusCode().value());
        assertNotNull(transactionResponse.getBody());
    }

    @Test
    void shouldFindTransactionByDateWithParams() {
        transactionRepository.saveAll(transactionMockList());

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("month", 1);
        parameters.add("year", 2022);
        parameters.add("keyword", null);
        parameters.add("transactionType", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", token);

        HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions/filter",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(200, transactionResponse.getStatusCode().value());
    }

    @Test
    @DisplayName(value = "Create transaction paid true with 100$ and change to paid false should remove amount from account.")
    void shouldUpdateTransaction() {
        final var transaction = new TransactionPostRequestDTO(
                account.getId(),
                100.00D,
                true,
                "Arroz",
                LocalDate.of(2022, 10, 10),
                TransactionType.REVENUE.name(),
                TransactionCategory.RESTAURANT.name(),
                null,
                false
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(transaction, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
                HttpMethod.POST,
                request,
                TransactionResponseDTO.class);

        final Account acc = accountGateway.findById(account.getId());

        //Account paid true
        assertEquals(0, new BigDecimal(100).compareTo(acc.getAmount()));

        final var transactionPut = new TransactionPutRequesDTO(
               account.getId(),
                transactionResponse.getBody().id(),
                transactionResponse.getBody().amount(),
                false,
                transactionResponse.getBody().description(),
                transactionResponse.getBody().date(),
                transactionResponse.getBody().transactionType(),
                transactionResponse.getBody().transactionCategory(),
                transactionResponse.getBody().bankSlip(),
                transactionResponse.getBody().barCode()
        );

        final var request2 = new HttpEntity<>(transactionPut, headers);

        final var update = testRestTemplate.exchange("/v1/transactions",
                HttpMethod.PUT,
                request2,
                TransactionResponseDTO.class
        );

        final Account acc02 = accountGateway.findById(account.getId());

        //Account paid false
        assertEquals(0, new BigDecimal(0).compareTo(acc02.getAmount()));
    }

    @Test
    void deleteById() {
    }
}