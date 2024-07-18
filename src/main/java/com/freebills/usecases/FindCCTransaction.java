package com.freebills.usecases;

import com.freebills.domain.CCTransaction;
import com.freebills.gateways.CCTransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindCCTransaction {

    private final CCTransactionGateway ccTransactionGateway;

    public Page<CCTransaction> findAllWithFilters(final Long cardId, final String username, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        return ccTransactionGateway.findAllWithFilters(cardId, username, month, year, pageable, keyword);
    }

    public CCTransaction findById(final Long id, String username) {
        return ccTransactionGateway.findById(id, username);
    }

    public CCTransaction findById(final Long id) {
        return ccTransactionGateway.findById(id);
    }
}
