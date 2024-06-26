package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.LoginRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequestDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.entities.TransactionEntity;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.EventRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import com.freebills.usecases.FindAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionEntityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FindAccount accountGateway;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EventGateway eventGateway;

    private static AccountEntity accountEntity;

    private static String token;

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

        final Event event = new Event();
        event.setEventType(EventType.TRANSACTION_CREATED);
        event.setAggregateId(accountEntity.getId());
        event.setTransactionData(new Transaction(BigDecimal.valueOf(0)));

        eventGateway.save(event);

        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);
    }

    @AfterEach
    void afterSetup() {
        transactionRepository.deleteAll();
        accountsRepository.deleteAll();
        eventRepository.deleteAll();
    }

    private List<TransactionEntity> transactionMockList() {
        TransactionEntity t1 = new TransactionEntity();
        t1.setAmount(new BigDecimal("100"));
        t1.setDate(LocalDate.of(2022, 1, 1));
        t1.setDescription("Arroz");
        t1.setBarCode(null);
        t1.setBankSlip(false);
        t1.setTransactionType(TransactionType.REVENUE);
        t1.setTransactionCategory(TransactionCategory.HOUSE);
        t1.setPaid(false);
        t1.setAccount(accountEntity);

        TransactionEntity t2 = new TransactionEntity();
        t2.setAmount(new BigDecimal("100"));
        t2.setDate(LocalDate.of(2022, 1, 1));
        t2.setDescription("Arroz");
        t2.setBarCode(null);
        t2.setBankSlip(false);
        t2.setTransactionType(TransactionType.REVENUE);
        t2.setTransactionCategory(TransactionCategory.HOUSE);
        t2.setPaid(false);
        t2.setAccount(accountEntity);

        TransactionEntity t3 = new TransactionEntity();
        t3.setAmount(new BigDecimal("100"));
        t3.setDate(LocalDate.of(2022, 1, 1));
        t3.setDescription("Arroz");
        t3.setBarCode(null);
        t3.setBankSlip(false);
        t3.setTransactionType(TransactionType.REVENUE);
        t3.setTransactionCategory(TransactionCategory.HOUSE);
        t3.setPaid(false);
        t3.setAccount(accountEntity);

        TransactionEntity t4 = new TransactionEntity();
        t4.setAmount(new BigDecimal("100"));
        t4.setDate(LocalDate.of(2022, 1, 1));
        t4.setDescription("Arroz");
        t4.setBarCode(null);
        t4.setBankSlip(false);
        t4.setTransactionType(TransactionType.REVENUE);
        t4.setTransactionCategory(TransactionCategory.HOUSE);
        t4.setPaid(false);
        t4.setAccount(accountEntity);

        return List.of(t1, t2, t3, t4);
    }

//    @Test
//    void shouldSaveOneTransactionRevenue() {
//        final var transaction = new TransactionPostRequestDTO(
//                accountEntity.getId(),
//                100.00D,
//                true,
//                "Arroz",
//                LocalDate.of(2022, 10, 10),
//                TransactionType.REVENUE.name(),
//                TransactionCategory.RESTAURANT.name(),
//                1,
//                "",
//                ""
//        );
//
//        final var headers = new HttpHeaders();
//        headers.set("Cookie", token);
//        final var request = new HttpEntity<>(transaction, headers);
//
//        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
//                HttpMethod.POST,
//                request,
//                TransactionResponseDTO.class);
//
//        final var acc = accountGateway.byId(accountEntity.getId());
//
//        //Account
//        assertEquals(0, new BigDecimal(100).compareTo(acc.getAmount()));
//
//        assertEquals(HttpStatus.CREATED.value(), transactionResponse.getStatusCode().value());
//        assertNotNull(transactionResponse.getBody());
//    }
//
//    @Test
//    void shouldSaveOneTransactionRevenueNotPaid() {
//        final var transaction = new TransactionPostRequestDTO(
//                accountEntity.getId(),
//                100.00D,
//                false,
//                "Arroz",
//                LocalDate.of(2022, 10, 10),
//                TransactionType.REVENUE.name(),
//                TransactionCategory.RESTAURANT.name(),
//                null,
//                false
//        );
//
//        final var headers = new HttpHeaders();
//        headers.set("Cookie", token);
//        final var request = new HttpEntity<>(transaction, headers);
//
//        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
//                HttpMethod.POST,
//                request,
//                TransactionResponseDTO.class);
//
//        final var acc = accountGateway.byId(accountEntity.getId());
//
//        //Account
//        assertEquals(0, new BigDecimal(0).compareTo(acc.getAmount()));
//
//        assertEquals(HttpStatus.CREATED.value(), transactionResponse.getStatusCode().value());
//        assertNotNull(transactionResponse.getBody());
//    }
//
//    @Test
//    void shouldSaveOneTransactionExpense() {
//        final var transaction = new TransactionPostRequestDTO(
//                accountEntity.getId(),
//                100.00D,
//                true,
//                "Arroz",
//                LocalDate.of(2022, 10, 10),
//                TransactionType.EXPENSE.name(),
//                TransactionCategory.RESTAURANT.name(),
//                null,
//                false
//        );
//
//        final var headers = new HttpHeaders();
//        headers.set("Cookie", token);
//        final var request = new HttpEntity<>(transaction, headers);
//
//        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
//                HttpMethod.POST,
//                request,
//                TransactionResponseDTO.class);
//
//        final var acc = accountGateway.byId(accountEntity.getId());
//
//        //Account
//        assertEquals(0, new BigDecimal(-100).compareTo(acc.getAmount()));
//
//        assertEquals(HttpStatus.CREATED.value(), transactionResponse.getStatusCode().value());
//        assertNotNull(transactionResponse.getBody());
//    }

    @Test
    void shouldFindTransactionById() {
        final List<TransactionEntity> transactionEntities = transactionRepository.saveAll(transactionMockList());
        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(null, headers);

        final var transactionResponse = testRestTemplate.exchange("/v1/transactions/" + transactionEntities.get(0).getId(),
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

//    @Test
//    @DisplayName(value = "Create transaction paid true with 100$ and change to paid false should remove amount from account.")
//    void shouldUpdateTransaction() {
//        final var transaction = new TransactionPostRequestDTO(
//                accountEntity.getId(),
//                100.00D,
//                true,
//                "Arroz",
//                LocalDate.of(2022, 10, 10),
//                TransactionType.REVENUE.name(),
//                TransactionCategory.RESTAURANT.name(),
//                null,
//                false
//        );
//
//        final var headers = new HttpHeaders();
//        headers.set("Cookie", token);
//        final var request = new HttpEntity<>(transaction, headers);
//
//        final var transactionResponse = testRestTemplate.exchange("/v1/transactions",
//                HttpMethod.POST,
//                request,
//                TransactionResponseDTO.class);
//
//        final var acc = accountGateway.byId(accountEntity.getId());
//
//        //Account paid true
//        assertEquals(0, new BigDecimal(100).compareTo(acc.getAmount()));
//
//        final var transactionPut = new TransactionPutRequestDTO(
//               accountEntity.getId(),
//                transactionResponse.getBody().id(),
//                transactionResponse.getBody().amount(),
//                false,
//                transactionResponse.getBody().description(),
//                transactionResponse.getBody().date(),
//                transactionResponse.getBody().transactionType(),
//                transactionResponse.getBody().transactionCategory(),
//                transactionResponse.getBody().bankSlip(),
//                transactionResponse.getBody().barCode()
//        );
//
//        final var request2 = new HttpEntity<>(transactionPut, headers);
//
//        final var update = testRestTemplate.exchange("/v1/transactions",
//                HttpMethod.PUT,
//                request2,
//                TransactionResponseDTO.class
//        );
//
//        final var acc02 = accountGateway.byId(accountEntity.getId());
//
//        //Account paid false
//        assertEquals(0, new BigDecimal(0).compareTo(acc02.getAmount()));
//    }
}