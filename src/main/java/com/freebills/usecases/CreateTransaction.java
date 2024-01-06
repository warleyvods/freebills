package com.freebills.usecases;

import com.freebills.entities.Transaction;
import com.freebills.entities.TransactionLog;
import com.freebills.gateways.TransactionGateway;
import com.freebills.repositories.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionValidation transactionValidation;
    private final TransactionLogRepository transactionLogRepository;

    public Transaction execute(final Transaction transaction) {
        if (transaction.getPreviousAmount() == null) {
            transaction.setPreviousAmount(transaction.getAmount());
        }

        final Transaction transactionSaved = transactionGateway.save(transaction);
        transactionLogRepository.save(new TransactionLog(
                        transactionSaved.getAmount(),
                        null,
                        transactionSaved.getAccount().getId(),
                        null,
                        transactionSaved.getAccount().getId(),
                        transactionSaved.getAmount(),
                        transactionSaved,
                null,
                transactionSaved.getTransactionType()
                )
        );
        log.info("[createTransaction:{}] Creating new transaction", transactionSaved.getId());
        transactionValidation.transactionCreationValidation(transactionSaved);
        return transactionSaved;
    }
}
