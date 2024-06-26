package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.UserGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record FindTransaction(TransactionGateway transactionGateway, UserGateway userGateway) {

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
}
