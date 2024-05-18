package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.mapper.TransactionGatewayMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionValidation transactionValidation;
    private final TransactionGatewayMapper transactionGatewayMapper;
    private final EntityManager entityManager;

    public Transaction execute(final Transaction transaction) {
        return null;
    }
}
