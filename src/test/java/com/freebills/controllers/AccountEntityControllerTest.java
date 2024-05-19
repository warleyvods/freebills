package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.LoginRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountEntityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static String token;

    private static UserEntity userEntity;

    @BeforeEach
    void beforeSetup() {
        userEntity = userRepository.findById(1L).orElse(null);
        accountsRepository.deleteAll();
        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/v1/auth/login", request, Object.class);
        token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);
    }

    @Test
    void shoudSaveAnAccount() {
        var account = new AccountPostRequestDTO(
                1L,
                300D,
                "Conta Inter",
                "MONEY",
                true,
                true,
                "INTER"
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(account, headers);

        var accountResponse = testRestTemplate.postForEntity("/v1/accounts", request, AccountResponseDTO.class);
        assertNotNull(accountResponse.getBody());
        assertEquals(account.amount(), accountResponse.getBody().amount());
        assertEquals(account.description(), accountResponse.getBody().description());
        assertEquals(account.accountType(), accountResponse.getBody().accountType());
        assertEquals(account.dashboard(), accountResponse.getBody().dashboard());
        assertEquals(account.archived(), accountResponse.getBody().archived());
        assertEquals(account.bankType(), accountResponse.getBody().bankType());

        assertEquals(201, accountResponse.getStatusCode().value());
    }

    @Test
    void shouldfindAllAccountsNonArchived() {
    }

    @Test
    void findAllAccountsArchived() {
    }

    @Test
    void shouldFindAccountById() {
    }

    @Test
    void shouldUpdateAnAccount() {
    }

    @Disabled
    @Test
    void shouldUpdateToArchiveAccount() {
    }

    @Disabled
    @Test
    void shouldUpdateToArchiveAccountToTrue() throws IOException {
    }

    @Test
    void shouldDeleteAccount() {
    }

    @Test
    void shouldReajustAmountIfValueIsLessThanActualAmount() {
    }

    @Test
    void shouldReajustAmountIfValueIsBiggestThanActualAmount() {
    }

    @Test
    void shouldReajustAmountIfValueIsZero() {
    }
}