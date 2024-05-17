package com.freebills.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freebills.controllers.dtos.requests.*;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.TransactionRepository;
import com.freebills.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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
                "INTER"
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(account, headers);

        var accountResponse = testRestTemplate.postForEntity("/v1/accounts", request, AccountResponseDTO.class);
        assertNotNull(accountResponse.getBody());
        assertEquals(201, accountResponse.getStatusCodeValue());
    }

    @Test
    void shouldfindAllAccountsNonArchived() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final var acc02 = new AccountEntity();
        acc02.setAmount(new BigDecimal("100"));
        acc02.setDescription("Conta Inter");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.NUBANK);
        acc02.setUser(userEntity);
        acc02.setArchived(false);
        acc02.setDashboard(false);

        accountsRepository.saveAll(List.of(acc01, acc02));

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(null, headers);

        var accountResponse = testRestTemplate.exchange("/v1/accounts", HttpMethod.GET, request, new ParameterizedTypeReference<List<AccountResponseDTO>>(){});

        assertEquals(HttpStatus.OK.value(), accountResponse.getStatusCodeValue());
        assertEquals(2, Objects.requireNonNull(accountResponse.getBody()).size());
    }

    @Test
    void findAllAccountsArchived() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final var acc02 = new AccountEntity();
        acc02.setAmount(new BigDecimal("100"));
        acc02.setDescription("Conta Inter");
        acc02.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc02.setBankType(BankType.NUBANK);
        acc02.setUser(userEntity);
        acc02.setArchived(true);
        acc02.setDashboard(false);

        accountsRepository.saveAll(List.of(acc01, acc02));

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(null, headers);

        var accountResponse = testRestTemplate.exchange("/v1/accounts/archived", HttpMethod.GET, request, new ParameterizedTypeReference<List<AccountResponseDTO>>(){});

        assertEquals(HttpStatus.OK.value(), accountResponse.getStatusCodeValue());
        assertEquals(2, Objects.requireNonNull(accountResponse.getBody()).size());
    }

    @Test
    void shouldFindAccountById() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(null, headers);

        var accountResponse = testRestTemplate.exchange("/v1/accounts/" + savedAccountEntity.getId(), HttpMethod.GET, request, AccountResponseDTO.class);
        assertEquals(200, accountResponse.getStatusCodeValue());
    }

    @Test
    void shouldUpdateAnAccount() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        var account = new AccountPutRequestDTO(
                savedAccountEntity.getId(),
                "Conta Nubanco",
                savedAccountEntity.getAccountType().name(),
                savedAccountEntity.isDashboard(),
                savedAccountEntity.getBankType().name()
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(account, headers);

        final var accountResponse = testRestTemplate.exchange("/v1/accounts", HttpMethod.PUT, request, AccountResponseDTO.class);
        assertEquals(200, accountResponse.getStatusCodeValue());
        assertNotNull(accountResponse.getBody());
        assertEquals("Conta Nubanco", accountResponse.getBody().description());
    }

    @Disabled
    @Test
    void shouldUpdateToArchiveAccount() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        var account = new AccountPatchArchivedRequestDTO(
                savedAccountEntity.getId(),
                false
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(account, headers);

        final var accountResponse = testRestTemplate.exchange("/v1/accounts", HttpMethod.PATCH, request, AccountResponseDTO.class);

        assertEquals(200, accountResponse.getStatusCodeValue());
        assertNotNull(accountResponse.getBody());
        assertFalse(accountResponse.getBody().archived());
    }

    @Disabled
    @Test
    void shouldUpdateToArchiveAccountToTrue() throws IOException {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        var account = new AccountPatchArchivedRequestDTO(
                savedAccountEntity.getId(),
                true
        );

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(account, headers);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPatch httpPatch = new HttpPatch("http://localhost:8080/v1/accounts");
        httpPatch.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(account), ContentType.APPLICATION_JSON));
        httpPatch.setHeader("Cookie", token);
        HttpResponse response = httpClient.execute(httpPatch);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertNotNull(response.getEntity());
        assertTrue(IOUtils.toString(response.getEntity().getContent()).contains("archived\":true"));
    }

    @Test
    void shouldDeleteAccount() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(null, headers);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        final var accountResponse = testRestTemplate.exchange("/v1/accounts/" + savedAccountEntity.getId(), HttpMethod.DELETE, request, Void.class);
        final AccountEntity accountEntity = accountsRepository.findById(savedAccountEntity.getId()).orElse(null);

        assertEquals(204, accountResponse.getStatusCodeValue());
        assertNull(accountEntity);
    }

    @Disabled
    @Test
    void shouldReajustAmountIfValueIsLessThanActualAmount() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        final var reajustBody = new AccountReajustDTO(savedAccountEntity.getId(), new BigDecimal("50"), "true");

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(reajustBody, headers);

        final var accountResponse = testRestTemplate.exchange("/v1/accounts/readjustment", HttpMethod.PATCH, request, Void.class);

        final AccountEntity accountEntity = accountsRepository.findById(savedAccountEntity.getId()).orElse(null);
        final var transaction = transactionRepository.findByTransactionFilterByDate("admin", null, null, null, null, null).getContent();

        assertEquals(1, transaction.size());
        assertEquals("Reajuste", transaction.get(0).getDescription());
        assertEquals(0, new BigDecimal(50).compareTo(transaction.get(0).getAmount()));
        assertEquals(TransactionType.EXPENSE, transaction.get(0).getTransactionType());
        assertEquals(TransactionCategory.REAJUST, transaction.get(0).getTransactionCategory());

        assertEquals(200, accountResponse.getStatusCodeValue());
        assertEquals(0, new BigDecimal(50).compareTo(accountEntity.getAmount()));
    }

    @Disabled
    @Test
    void shouldReajustAmountIfValueIsBiggestThanActualAmount() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        final var reajustBody = new AccountReajustDTO(savedAccountEntity.getId(), new BigDecimal("200"), "true");

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(reajustBody, headers);

        final var accountResponse = testRestTemplate.exchange("/v1/accounts/readjustment", HttpMethod.PATCH, request, Void.class);

        final AccountEntity accountEntity = accountsRepository.findById(savedAccountEntity.getId()).orElse(null);
        final var transaction = transactionRepository.findByTransactionFilterByDate("admin", null, null, null, null, null).getContent();

        assertEquals(1, transaction.size());
        assertEquals("Reajuste", transaction.get(0).getDescription());
        assertEquals(0, new BigDecimal(100).compareTo(transaction.get(0).getAmount()));
        assertEquals(TransactionType.REVENUE, transaction.get(0).getTransactionType());
        assertEquals(TransactionCategory.REAJUST, transaction.get(0).getTransactionCategory());

        assertEquals(200, accountResponse.getStatusCodeValue());
        assertEquals(0, new BigDecimal(200).compareTo(accountEntity.getAmount()));
    }

    @Disabled
    @Test
    void shouldReajustAmountIfValueIsZero() {
        final var acc01 = new AccountEntity();
        acc01.setAmount(new BigDecimal("100"));
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(true);
        acc01.setDashboard(false);

        final AccountEntity savedAccountEntity = accountsRepository.save(acc01);

        final var reajustBody = new AccountReajustDTO(savedAccountEntity.getId(), new BigDecimal("0"), "true");

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var request = new HttpEntity<>(reajustBody, headers);

        final var accountResponse = testRestTemplate.exchange("/v1/accounts/readjustment", HttpMethod.PATCH, request, Void.class);

        final AccountEntity accountEntity = accountsRepository.findById(savedAccountEntity.getId()).orElse(null);
        final var transaction = transactionRepository.findByTransactionFilterByDate("admin", null, null, null, null, null).getContent();

        assertEquals(1, transaction.size());
        assertEquals("Reajuste", transaction.get(0).getDescription());
        assertEquals(0, new BigDecimal(100).compareTo(transaction.get(0).getAmount()));
        assertEquals(TransactionType.EXPENSE, transaction.get(0).getTransactionType());
        assertEquals(TransactionCategory.REAJUST, transaction.get(0).getTransactionCategory());

        assertEquals(200, accountResponse.getStatusCodeValue());
        assertEquals(0, new BigDecimal(0).compareTo(accountEntity.getAmount()));
    }
}