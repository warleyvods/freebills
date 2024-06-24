package com.freebills.usecases;

import com.freebills.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DuplicateTransactionTest {

    @Mock
    private CreateTransaction createTransaction;

    @Mock
    private FindTransaction findTransaction;

    @InjectMocks
    private DuplicateTransaction duplicateTransaction;

    @Test
    void shouldDuplicateTransaction() {
        Transaction transaction = Transaction.builder()
                .date(LocalDate.of(2020, 1, 10))
                .amount(new BigDecimal(0))
                .build();

        Transaction expected = Transaction.builder()
                .date(LocalDate.of(2020, 2, 10))
                .amount(new BigDecimal(0))
                .build();

        when(findTransaction.findById(any(Long.class))).thenReturn(transaction);

        when(createTransaction.execute(any(Transaction.class))).thenReturn(expected);

        final Transaction response = duplicateTransaction.execute(1L);

        assertEquals(response.getDate(), transaction.getDate().plusMonths(1));
    }
}
