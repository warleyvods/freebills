package com.freebills.gateways;

import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.TransactionEntity;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.exceptions.LoginInvalidException;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.mapper.TransactionGatewayMapper;
import com.freebills.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionGateway {

    private final TransactionRepository transactionRepository;
    private final TransactionGatewayMapper transactionGatewayMapper;

    public Transaction save(final Transaction transaction) {
        return transactionGatewayMapper.toDomain(transactionRepository.save(transactionGatewayMapper.toEntity(transaction)));
    }

    public Page<Transaction> findTransactionsWithFilters(final String login,
                                                         final Integer month,
                                                         final Integer year,
                                                         final Pageable pageable,
                                                         final String keyword,
                                                         final TransactionType transactionType) {
        assertLoginIsValid(login);

        var transactionsPage = transactionRepository.findByTransactionFilterByDate(login, month, year, keyword, transactionType, pageable);
        return transactionsPage.map(transactionGatewayMapper::toDomain);
    }

    public Transaction findById(Long id) {
        return transactionGatewayMapper.toDomain(transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transaction not found!")));
    }

    public Transaction update(final Transaction transaction) {
        final TransactionEntity entity = transactionGatewayMapper.toEntity(transaction);
        final TransactionEntity save = transactionRepository.save(entity);
        return transactionGatewayMapper.toDomain(save);
    }

    public void delete(final Long id) {
        transactionRepository.deleteById(id);
    }

    private void assertLoginIsValid(final String login) {
        if (login == null) throw new LoginInvalidException("Login invalid!");
    }
}
