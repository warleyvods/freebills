package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.UserGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindTransaction {

    private final TransactionGateway transactionGateway;
    private final UserGateway userGateway;

    public Transaction findById(final Long id) {
        return transactionGateway.findById(id);
    }

    public Page<Transaction> findAllWithFilters(final String login,
                                                final Integer month,
                                                final Integer year,
                                                final Pageable pageable,
                                                final String keyword,
                                                final TransactionType transactionType) {

        return transactionGateway.findTransactionsWithFilters(login, month, year, pageable, keyword, transactionType);
    }

    public Page<Transaction> findAllByCategory(final String login,
                                               final Integer month,
                                               final Integer year,
                                               final String category,
                                               final Pageable pageable,
                                               final TransactionType transactionType) {
        return transactionGateway.findAllByCategory(login, month, year, category, transactionType, pageable);
    }
}
