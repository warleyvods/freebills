package com.freebills.gateways;

import com.freebills.exceptions.LoginInvalidException;
import com.freebills.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertThrows;



@SpringBootTest
class TransactionGatewayTest {

    @Autowired
    private TransactionGateway transactionGateway;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void findByUserDateFilterWithInvalidLogin() {
        Integer month = 5;
        Integer year = 2024;
        Pageable pageable = PageRequest.of(0, 10);

//        assertThrows(LoginInvalidException.class, () -> transactionGateway.findByUserDateFilter(null, month, year, pageable, null, null));
    }
}
