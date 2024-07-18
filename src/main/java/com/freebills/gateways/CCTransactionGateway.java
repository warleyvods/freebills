package com.freebills.gateways;

import com.freebills.domain.CCTransaction;
import com.freebills.exceptions.LoginInvalidException;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.entities.CCTransactionEntity;
import com.freebills.gateways.mapper.CCTransactionGatewayMapper;
import com.freebills.repositories.CCTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CCTransactionGateway {

    private final CCTransactionsRepository ccTransactionsRepository;
    private final CCTransactionGatewayMapper ccTransactionGatewayMapper;

    public CCTransaction save(final CCTransaction ccTransaction) {
        return ccTransactionGatewayMapper.toDomain(ccTransactionsRepository.save(ccTransactionGatewayMapper.toEntity(ccTransaction)));
    }

    public Page<CCTransaction> findAllWithFilters(final Long cardId,
                                                  final String login,
                                                  final Integer month,
                                                  final Integer year,
                                                  final Pageable pageable,
                                                  final String keyword) {
        assertLoginIsValid(login);

        var transactionsPage = ccTransactionsRepository.findAllWithFilters(cardId, login, month, year, keyword, pageable);
        return transactionsPage.map(ccTransactionGatewayMapper::toDomain);
    }

    public CCTransaction findById(final Long id, final String username) {
        return ccTransactionGatewayMapper.toDomain(ccTransactionsRepository.findByIdAndUsername(id, username).orElseThrow(() -> new TransactionNotFoundException("Transaction not found!")));
    }

    public CCTransaction findById(final Long id) {
        return ccTransactionGatewayMapper.toDomain(ccTransactionsRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transaction not found!")));
    }

    public CCTransaction update(final CCTransaction ccTransaction) {
        final CCTransactionEntity entity = ccTransactionGatewayMapper.toEntity(ccTransaction);
        final CCTransactionEntity save = ccTransactionsRepository.save(entity);
        return ccTransactionGatewayMapper.toDomain(save);
    }

    @CacheEvict(value = "ccTransaction", allEntries = true)
    public void delete(final Long id) {
        ccTransactionsRepository.deleteById(id);
    }

    private void assertLoginIsValid(final String login) {
        if (login == null) throw new LoginInvalidException("Login invalid!");
    }
}
