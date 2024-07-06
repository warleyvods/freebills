package com.freebills.repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("tc")
@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

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
        eventRepository.deleteAll();
    }

    @Test
    void test() {
//        final TransferJsonData transferJsonData = new TransferJsonData();
//        transferJsonData.setId(1L);
//        transferJsonData.setObservation("ANY");
//        transferJsonData.setDescription("ANY");
//        transferJsonData.setTransferCategory("ANY");
//        transferJsonData.setFromAccountId(1L);
//        transferJsonData.setToAccountId(2L);
//        transferJsonData.setUpdatedAt(LocalDateTime.now());
//        transferJsonData.setCreatedAt(LocalDateTime.now());
//
//
//        EventEntity event = new EventEntity();
//        event.setAggregateId(0L);
//        event.setAmount(new BigDecimal("100"));
//        event.setEventType(TRANSFER);
//        event.setTransferType(IN);
//        event.setTransactionData(null);
//        event.setOldTransactionData(null);
//        event.setTransferJsonData(transferJsonData);
//        event.setCreatedAt(now());
//
//        final EventEntity save = eventRepository.save(event);
//
//        assertEquals(transferJsonData.getId(), save.getTransferJsonData().getId());
    }
}