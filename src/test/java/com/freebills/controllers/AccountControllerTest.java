package com.freebills.controllers;

import com.freebills.controllers.mappers.AccountMapper;
import com.freebills.usecases.AdjustAccount;
import com.freebills.usecases.CreateAccount;
import com.freebills.usecases.DeleteAccount;
import com.freebills.usecases.FindAccount;
import com.freebills.usecases.UpdateAccount;
import com.freebills.utils.TestContainerBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class AccountControllerTest extends TestContainerBase {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountMapper mapper;

    @Autowired
    private FindAccount findAccount;

    @Autowired
    private UpdateAccount updateAccount;

    @Autowired
    private CreateAccount createAccount;

    @Autowired
    private DeleteAccount deleteAccount;

    @Autowired
    private AdjustAccount adjustAccount;

    @Test
    void shouldSaveAccount() {

    }
}
