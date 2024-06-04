package com.freebills.usecases;

import com.freebills.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DuplicateTransaction {

    private final CreateTransaction createTransaction;
    private final FindTransaction findTransaction;

    public Transaction execute(final Long id) {
        final Transaction transaction = findTransaction.findById(id);

        final Transaction copy = transaction.toBuilder()
                .id(null)
                .date(transaction.getDate().plusMonths(1))
                .amount(new BigDecimal(0))
                .paid(false)
                .build();

        return createTransaction.execute(copy);
    }
}
