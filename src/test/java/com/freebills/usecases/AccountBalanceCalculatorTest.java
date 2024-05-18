package com.freebills.usecases;

import java.time.LocalDate;

import com.freebills.gateways.entities.enums.TransactionCategory;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountBalanceCalculatorTest {

    @Autowired
    private AccountBalanceCalculator accountBalanceCalculator;

    @Autowired
    private EventGateway eventGateway;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    private Transaction createTransaction(BigDecimal value, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setAmount(value);
        transaction.setDate(LocalDate.now());
        transaction.setDescription("TESTE");
        transaction.setBankSlip(false);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);

        return transaction;
    }

    private List<Event> mockedEvents1() {
        Event event1 = new Event();
        event1.setAggregateId(1L);
        event1.setEventType(EventType.TRANSACTION_CREATED);
        event1.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        Event event2 = new Event();
        event2.setAggregateId(1L);
        event2.setEventType(EventType.TRANSACTION_CREATED);
        event2.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        Event event3 = new Event();
        event3.setAggregateId(1L);
        event3.setEventType(EventType.TRANSACTION_CREATED);
        event3.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        return List.of(event1, event2, event3);
    }

    private List<Event> mockedEvents2() {
        Event event1 = new Event();
        event1.setAggregateId(1L);
        event1.setEventType(EventType.TRANSACTION_CREATED);
        event1.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        Event event2 = new Event();
        event2.setAggregateId(1L);
        event2.setEventType(EventType.TRANSACTION_DELETED);
        event2.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        Event event3 = new Event();
        event3.setAggregateId(1L);
        event3.setEventType(EventType.TRANSACTION_CREATED);
        event3.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        return List.of(event1, event2, event3);
    }

    private Event createEvent(Long aggregateId, EventType eventType, BigDecimal value, TransactionType transactionType) {
        Event event = new Event();
        event.setAggregateId(aggregateId);
        event.setEventType(eventType);
        event.setTransactionData(createTransaction(value, transactionType));

        return event;
    }

    private Event updateEvent(Long aggregateId, BigDecimal oldAmount, BigDecimal newAmount, TransactionType oldTransactionType, TransactionType newTransactionType) {
        Event event = new Event();
        event.setAggregateId(aggregateId);
        event.setEventType(EventType.TRANSACTION_UPDATED);

        event.setOldTransactionData(createTransaction(oldAmount, oldTransactionType));
        event.setTransactionData(createTransaction(newAmount, newTransactionType));
        return event;
    }

    @Test
    void testCalculateBalanceForAccount_withCreatedEvents() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = mockedEvents1();
        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("300"), balance);
    }

    @Test
    void testCalculateBalanceForAccount0() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("500"), balance);
    }

    @Test
    void testCalculateBalanceForAccount1() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("200"), balance);
    }

    @Test
    void testCalculateBalanceForAccount2() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("0"), balance);
    }

    @Test
    void testCalculateBalanceForAccount3() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("0"), balance);
    }

    @Test
    void testCalculateBalanceForAccount4() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("100"), balance);
    }

    @Test
    void testCalculateBalanceForAccount5() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("-100"), balance);
    }

    @Test
    void testCalculateBalanceForAccount6() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("100"), balance);
    }

    @Test
    void testCalculateBalanceForAccount7() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),

                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("0"), balance);
    }

    @Test
    void testCalculateBalanceForAccount8() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),

                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, EventType.TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("300"), balance);
    }

    @Test
    void testCalculateBalanceForAccount9_update() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = List.of(
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, EventType.TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),

                updateEvent(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TransactionType.REVENUE, TransactionType.EXPENSE),
                updateEvent(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TransactionType.REVENUE, TransactionType.EXPENSE),
                updateEvent(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TransactionType.REVENUE, TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("-300"), balance);
    }
}
