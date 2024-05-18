package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
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

    private List<Event> mockedEvents1() {
        Event event1 = new Event();
        event1.setAggregateId(1L);
        event1.setEventType(EventType.TRANSACTION_CREATED);
        event1.setTransactionAmount(BigDecimal.valueOf(100));
        event1.setTransactionType(TransactionType.REVENUE);

        Event event2 = new Event();
        event2.setAggregateId(1L);
        event2.setEventType(EventType.TRANSACTION_CREATED);
        event2.setTransactionAmount(BigDecimal.valueOf(100));
        event2.setTransactionType(TransactionType.REVENUE);

        Event event3 = new Event();
        event3.setAggregateId(1L);
        event3.setEventType(EventType.TRANSACTION_CREATED);
        event3.setTransactionAmount(BigDecimal.valueOf(100));
        event3.setTransactionType(TransactionType.REVENUE);

        return List.of(event1, event2, event3);
    }

    private List<Event> mockedEvents2() {
        Event event1 = new Event();
        event1.setAggregateId(1L);
        event1.setEventType(EventType.TRANSACTION_CREATED);
        event1.setTransactionAmount(BigDecimal.valueOf(100));
        event1.setTransactionType(TransactionType.REVENUE);

        Event event2 = new Event();
        event2.setAggregateId(1L);
        event2.setEventType(EventType.TRANSACTION_DELETED);
        event2.setTransactionAmount(BigDecimal.valueOf(100));
        event2.setTransactionType(TransactionType.REVENUE);

        Event event3 = new Event();
        event3.setAggregateId(1L);
        event3.setEventType(EventType.TRANSACTION_CREATED);
        event3.setTransactionAmount(BigDecimal.valueOf(100));
        event3.setTransactionType(TransactionType.REVENUE);

        return List.of(event1, event2, event3);
    }

    private Event createEvent(Long agragateId, EventType eventType, BigDecimal value, TransactionType transactionType) {
        Event event = new Event();
        event.setAggregateId(agragateId);
        event.setEventType(eventType);
        event.setTransactionAmount(value);
        event.setTransactionType(transactionType);
        return event;
    }

    private Event updateEvent(Long aggregateId, BigDecimal oldAmount, BigDecimal newAmount, TransactionType oldTransactionType, TransactionType newTransactionType) {
        Event event = new Event();
        event.setAggregateId(aggregateId);
        event.setEventType(EventType.TRANSACTION_UPDATED);
        event.setOldTransactionType(oldTransactionType);
        event.setNewTransactionType(newTransactionType);

        event.setOldTransactionAmount(oldAmount);
        event.setNewTransactionAmount(newAmount);

        return event;
    }

    @Test
    void testCalculateBalanceForAccount_withCreatedEvents() {
        Account account = new Account();
        account.setId(1L);

        final List<Event> events = mockedEvents1();
        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("300.00"), balance);
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

        assertEquals(new BigDecimal("500.00"), balance);
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

        assertEquals(new BigDecimal("200.00"), balance);
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

        assertEquals(new BigDecimal("0.00"), balance);
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

        assertEquals(new BigDecimal("0.00"), balance);
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

        assertEquals(new BigDecimal("100.00"), balance);
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

        assertEquals(new BigDecimal("-100.00"), balance);
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

        assertEquals(new BigDecimal("100.00"), balance);
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

        assertEquals(new BigDecimal("0.00"), balance);
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

        assertEquals(new BigDecimal("300.00"), balance);
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

        assertEquals(new BigDecimal("-300.00"), balance);
    }
}
