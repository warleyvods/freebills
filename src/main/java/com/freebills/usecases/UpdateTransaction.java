package com.freebills.usecases;

import com.freebills.domains.Transaction;
import com.freebills.domains.TransactionLog;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.TransactionGateway;
import com.freebills.repositories.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionValidation transactionValidation;
    private final TransactionLogRepository transactionLogRepository;

    public Transaction execute(final Transaction transaction) {
        if (Boolean.FALSE.equals(transaction.getBankSlip())) {
            transaction.setBarCode(null);
        }

        final Transaction transactionSaved = transactionGateway.update(transaction);
        final TransactionLog lastTransactionLog = transactionLogRepository.findAll().stream().reduce((a, b) -> b)
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
