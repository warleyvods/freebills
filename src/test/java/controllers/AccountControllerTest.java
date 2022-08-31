package controllers;

import com.freebills.FreebillsApplication;
import com.freebills.auth.dtos.LoginRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.domains.Account;
import com.freebills.domains.User;
import com.freebills.domains.enums.AccountType;
import com.freebills.domains.enums.BankType;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FreebillsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserRepository userRepository;

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
    void shouldFindAll() {
        final var user = new User();
        user.setName("abcd");
        user.setLogin("abcd");
        user.setEmail("abcd@teste.com");
        user.setPassword(new BCryptPasswordEncoder().encode("123"));
        user.setAdmin(true);
        user.setActive(true);

        final User userSaved = userRepository.save(user);

        final var request = new HttpEntity<>(new LoginRequestDTO("abcd", "123"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);

        final var account = new Account();
        account.setAmount(BigDecimal.valueOf(500));
        account.setDescription("Conta Inter");
        account.setAccountType(AccountType.MONEY);
        account.setDashboard(true);
        account.setBankType(BankType.INTER);
        account.setUser(userSaved);

        final var account1 = new Account();
        account1.setAmount(BigDecimal.valueOf(500));
        account1.setDescription("Conta Inter");
        account1.setAccountType(AccountType.MONEY);
        account1.setDashboard(true);
        account1.setBankType(BankType.INTER);
        account1.setUser(userSaved);

        accountsRepository.saveAll(List.of(account, account1));

        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        var newReq = new HttpEntity<>(null, headers);

        final var response = testRestTemplate.exchange("/v1/accounts?userId="+ userSaved.getId() , HttpMethod.GET, newReq, String.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldUpdateAccount() {
        final var user = new User();
        user.setName("abcde");
        user.setLogin("abcde");
        user.setEmail("abcde@teste.com");
        user.setPassword(new BCryptPasswordEncoder().encode("123"));
        user.setAdmin(true);
        user.setActive(true);

        final User userSaved = userRepository.save(user);

        final var request = new HttpEntity<>(new LoginRequestDTO("abcde", "123"));
        ResponseEntity<Object> objectResponseEntity = testRestTemplate.postForEntity("/login", request, Object.class);
        String token = Objects.requireNonNull(objectResponseEntity.getHeaders().get("Set-Cookie")).get(0);

        final var account = new Account();
        account.setAmount(BigDecimal.valueOf(500));
        account.setDescription("Conta Inter");
        account.setAccountType(AccountType.MONEY);
        account.setDashboard(true);
        account.setBankType(BankType.INTER);
        account.setUser(userSaved);
        final Account accountSaved = accountsRepository.save(account);

        final var update = new AccountPutRequestDTO(accountSaved.getId(), 300D, "Conta Nubank", "MONEY", true, "NUBANK", userSaved.getId());
        final var headers = new HttpHeaders();
        headers.set("Cookie", token);
        final var newRequest = new HttpEntity<>(update, headers);
        final var response = testRestTemplate.exchange("/v1/accounts", HttpMethod.PUT, newRequest, AccountResponseDTO.class);

        assertEquals("Conta Nubank", Objects.requireNonNull(response.getBody()).description());
    }
}
