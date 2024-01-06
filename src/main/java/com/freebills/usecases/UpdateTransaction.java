package com.freebills.usecases;

import com.freebills.gateways.entities.Transaction;
import com.freebills.gateways.entities.TransactionLog;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.TransactionGateway;
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
    private final EntityManager entityManager;

    public Transaction execute(final Transaction transaction) {
        entityManager.clear();

        if (Boolean.FALSE.equals(transaction.getBankSlip())) {
            transaction.setBarCode(null);
        }

        if (transactionGateway.findById(transaction.getId()).equals(transaction)) {
            return null;

        } else {
            final Transaction transactionSaved = transactionGateway.update(transaction);
            final TransactionLog lastTransactionLog = transactionLogRepository.findAll()
                    .stream()
                    .reduce((a, b) -> b)
                    .orElseThrow(() -> new TransactionNotFoundException("not found!"));

            if (lastTransactionLog.getInicialAccount() != null) {
                transactionLogRepository.save(new TransactionLog(
                        null,
                        lastTransactionLog.getAtualAmount(),
                        null,
                        transactionSaved.getFromAccount(),
                        transactionSaved.getAccount().getId(),
                        transactionSaved.getAmount(),
                        transactionSaved,
                        lastTransactionLog.getActualTransactionType(),
                        transactionSaved.getTransactionType()
                ));
            } else {
                transactionLogRepository.save(new TransactionLog(
                        null,
                        lastTransactionLog.getAtualAmount(),
                        null,
                        lastTransactionLog.getAtualAccount(),
                        transactionSaved.getAccount().getId(),
                        transactionSaved.getAmount(),
                        transactionSaved,
                        lastTransactionLog.getActualTransactionType(),
                        transactionSaved.getTransactionType()
                ));
            }

            transactionValidation.transactionUpdateValidation(transactionSaved);
            return transactionSaved;
        }
    }
}
