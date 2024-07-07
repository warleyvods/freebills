package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.domain.Transfer;
import com.freebills.gateways.EventGateway;
import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.entities.enums.TransferType;
import com.freebills.repositories.EventRepository;
import com.freebills.utils.TestContainerBase;
import com.freebills.utils.TestRedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_CREATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSACTION_UPDATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_CREATED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_DELETED;
import static com.freebills.gateways.entities.enums.EventType.TRANSFER_UPDATED;
import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class AccountBalanceCalculatorTest extends TestContainerBase {

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
        Account account = new Account();
        account.setId(1L);

        Transaction transaction = new Transaction();
        transaction.setAmount(value);
        transaction.setDate(LocalDate.now());
        transaction.setDescription("TESTE");
        transaction.setBankSlip(false);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionCategory(TransactionCategory.HOUSE);
        transaction.setPaid(true);
        transaction.setAccount(account);

        return transaction;
    }

    private List<Event> mockedEvents1() {
        Event event1 = new Event();
        event1.setAggregateId(1L);
        event1.setEventType(TRANSACTION_CREATED);
        event1.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        Event event2 = new Event();
        event2.setAggregateId(1L);
        event2.setEventType(TRANSACTION_CREATED);
        event2.setTransactionData(createTransaction(BigDecimal.valueOf(100), TransactionType.REVENUE));

        Event event3 = new Event();
        event3.setAggregateId(1L);
        event3.setEventType(TRANSACTION_CREATED);
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

    private Transfer createTransfer(BigDecimal value, TransferType transferType, Long accountId) {
        Transfer transfer = new Transfer();
        transfer.setAmount(value);
        transfer.setObservation("TRANSFER");
        transfer.setDescription("TRANSFER");
        transfer.setDate(LocalDate.now());
        transfer.setTransferType(transferType);
        transfer.setFromAccountId(new Account(accountId));
        transfer.setToAccountId(new Account(accountId));
        transfer.setUpdatedAt(LocalDateTime.now());
        transfer.setCreatedAt(LocalDateTime.now());

        return transfer;
    }

    private Event createTransferEvent(Long aggregateId, EventType eventType, BigDecimal value, TransferType transferType) {
        Event event = new Event();
        event.setAggregateId(aggregateId);
        event.setEventType(eventType);
        event.setTransferData(createTransfer(value, transferType, aggregateId));

        return event;
    }

    private Event updateTransferEvent(Long aggregateId, Transfer newTransfer, Transfer oldTransfer) {
        Event event = new Event();
        event.setAggregateId(aggregateId);
        event.setEventType(TRANSFER_UPDATED);
        event.setTransferData(newTransfer);
        event.setOldTransferData(oldTransfer);

        return event;
    }

    private Event updateEvent(BigDecimal oldAmount, BigDecimal newAmount, TransactionType oldTransactionType, TransactionType newTransactionType) {
        Event event = new Event();
        event.setAggregateId(1L);
        event.setEventType(TRANSACTION_UPDATED);

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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE)
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
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE)
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
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),

                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.REVENUE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),

                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.EXPENSE),
                createEvent(1L, TRANSACTION_DELETED, BigDecimal.valueOf(100), TransactionType.EXPENSE)
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
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(100), TransactionType.REVENUE),

                updateEvent(BigDecimal.valueOf(100), BigDecimal.valueOf(100), TransactionType.REVENUE, TransactionType.EXPENSE),
                updateEvent(BigDecimal.valueOf(100), BigDecimal.valueOf(100), TransactionType.REVENUE, TransactionType.EXPENSE),
                updateEvent(BigDecimal.valueOf(100), BigDecimal.valueOf(100), TransactionType.REVENUE, TransactionType.EXPENSE)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);

        assertEquals(new BigDecimal("-300"), balance);
    }

    @Test
    @DisplayName("Deve alterar o valor da conta corrente com transferencias entre contas e atualização da transferencia")
    void calculateBalanceForAccountWithTransfer01() {
        Account account1 = new Account();
        account1.setId(1L);

        Account account2 = new Account();
        account2.setId(2L);


        final Event transferEvent1 = createTransferEvent(1L, TRANSFER_CREATED, BigDecimal.valueOf(100), OUT);
        final Transfer transferData1 = transferEvent1.getTransferData();

        final Event transferEvent2 = createTransferEvent(2L, TRANSFER_CREATED, BigDecimal.valueOf(100), IN);
        final Transfer transferData2 = transferEvent2.getTransferData();

        final Transfer newTransferData1 = transferData1.toBuilder().amount(new BigDecimal("50")).build();
        final Transfer newTransferData2 = transferData2.toBuilder().amount(new BigDecimal("50")).build();


        final List<Event> events = List.of(
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(200), TransactionType.REVENUE),
                createEvent(2L, TRANSACTION_CREATED, BigDecimal.valueOf(200), TransactionType.REVENUE),

                transferEvent1,
                transferEvent2,

                updateTransferEvent(1L, newTransferData1, transferData1),
                updateTransferEvent(2L, newTransferData2, transferData2)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance1 = accountBalanceCalculator.calculateBalanceForAccount(account1);
        BigDecimal balance2 = accountBalanceCalculator.calculateBalanceForAccount(account2);

        assertEquals(new BigDecimal("150"), balance1);
        assertEquals(new BigDecimal("250"), balance2);
    }

    @Test
    @DisplayName("Deve alterar o valor da conta corrente com transferencias entre contas e atualização da transferencia - Deletar deverá voltar o valor original")
    void calculateBalanceForAccountWithTransfer02() {
        Account account1 = new Account();
        account1.setId(1L);

        Account account2 = new Account();
        account2.setId(2L);


        final Event transferEvent1 = createTransferEvent(1L, TRANSFER_CREATED, BigDecimal.valueOf(100), OUT);
        final Transfer transferData1 = transferEvent1.getTransferData();

        final Event transferEvent2 = createTransferEvent(2L, TRANSFER_CREATED, BigDecimal.valueOf(100), IN);
        final Transfer transferData2 = transferEvent2.getTransferData();

        final Transfer newTransferData1 = transferData1.toBuilder().amount(new BigDecimal("50")).build();
        final Transfer newTransferData2 = transferData2.toBuilder().amount(new BigDecimal("50")).build();


        final List<Event> events = List.of(
                createEvent(1L, TRANSACTION_CREATED, BigDecimal.valueOf(200), TransactionType.REVENUE),
                createEvent(2L, TRANSACTION_CREATED, BigDecimal.valueOf(200), TransactionType.REVENUE),

                transferEvent1,
                transferEvent2,

                updateTransferEvent(1L, newTransferData1, transferData1),
                updateTransferEvent(2L, newTransferData2, transferData2),

                createTransferEvent(1L, TRANSFER_DELETED, BigDecimal.valueOf(50), IN),
                createTransferEvent(2L, TRANSFER_DELETED, BigDecimal.valueOf(50), OUT)
        );

        events.forEach(e -> eventGateway.save(e));

        BigDecimal balance1 = accountBalanceCalculator.calculateBalanceForAccount(account1);
        BigDecimal balance2 = accountBalanceCalculator.calculateBalanceForAccount(account2);

        assertEquals(new BigDecimal("200"), balance1);
        assertEquals(new BigDecimal("200"), balance2);
    }
}
