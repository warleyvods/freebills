package controllers;

import com.freebills.FreebillsApplication;
import com.freebills.auth.dtos.LoginRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.repositories.AccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FreebillsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static String token;

    @BeforeEach
    void setup() {
        accountsRepository.deleteAll();
        final var request = new HttpEntity<>(new LoginRequestDTO("admin", "baguvix"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/login", request, Object.class);
        token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);
    }

    @Test
    void shouldSaveAccount() {
        final var account = new AccountPostRequestDTO(1L, 500D, "Conta Inter", "MONEY", true, "INTER");
        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var newRequest = new HttpEntity<>(account, headers);

        final ResponseEntity<AccountResponseDTO> exchange = testRestTemplate.postForEntity("/v1/accounts", newRequest, AccountResponseDTO.class);

        assertEquals(201, exchange.getStatusCodeValue());
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }
}
