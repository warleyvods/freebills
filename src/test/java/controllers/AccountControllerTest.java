package controllers;

import com.freebills.FreebillsApplication;
import com.freebills.domains.Account;
import com.freebills.domains.enums.AccountType;
import com.freebills.domains.enums.BankType;
import com.freebills.gateways.AccountGateway;
import com.freebills.repositories.AccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FreebillsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountGateway accountGateway;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup(){
        accountsRepository.deleteAll();
    }

    @Test
    void shouldSaveAccount() {
        var account = new Account();
        account.setId(1L);
        account.setAmount(BigDecimal.valueOf(500));
        account.setDescription("Conta Nubank");
        account.setAccountType(AccountType.valueOf("MONEY"));
        account.setBankType(BankType.valueOf("NUBANK"));
        account.setDashboard(true);
        Account accountSaved = accountsRepository.save(account);

        assertEquals(account.getId(),accountSaved.getId());
        assertEquals(account.getAccountType(),accountSaved.getAccountType());
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
