package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.TransactionEntity;
import com.freebills.gateways.entities.TransactionLog;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.mapper.TransactionGatewayMapper;
import com.freebills.repositories.TransactionLogRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionValidation transactionValidation;
    private final TransactionLogRepository transactionLogRepository;
    private final TransactionGatewayMapper transactionGatewayMapper;
    private final EntityManager entityManager;

    public Transaction execute(final Transaction transaction) {
//        entityManager.clear();

        if (Boolean.FALSE.equals(transaction.getBankSlip())) {
            transaction.setBarCode(null);
        }

        //TODO - aqui tem um bug, FODASE
        if (transactionGateway.findById(transaction.getId()).equals(transaction)) {
            return null;

        } else {
            final var transactionEntitySaved = transactionGateway.update(transaction);
            final TransactionLog lastTransactionLog = transactionLogRepository.findAll()
                    .stream()
                    .reduce((a, b) -> b)
                    .orElseThrow(() -> new TransactionNotFoundException("not found!"));

            if (lastTransactionLog.getInicialAccount() != null) {
                transactionLogRepository.save(new TransactionLog(
                        null,
                        lastTransactionLog.getAtualAmount(),
                        null,
                        transactionEntitySaved.getFromAccount(),
                        transactionEntitySaved.getAccount().getId(),
                        transactionEntitySaved.getAmount(),
                        transactionGatewayMapper.toEntity(transactionEntitySaved),
                        lastTransactionLog.getActualTransactionType(),
                        transactionEntitySaved.getTransactionType()
                ));
            } else {
                transactionLogRepository.save(new TransactionLog(
                        null,
                        lastTransactionLog.getAtualAmount(),
                        null,
                        lastTransactionLog.getAtualAccount(),
                        transactionEntitySaved.getAccount().getId(),
                        transactionEntitySaved.getAmount(),
                        transactionGatewayMapper.toEntity(transactionEntitySaved),
                        lastTransactionLog.getActualTransactionType(),
                        transactionEntitySaved.getTransactionType()
                ));
            }

            transactionValidation.transactionUpdateValidation(transactionEntitySaved);
            return transactionEntitySaved;
        }
    }
}
