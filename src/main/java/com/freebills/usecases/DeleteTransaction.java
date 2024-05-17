package com.freebills.usecases;

import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.entities.TransactionEntity;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteTransaction(
        TransactionGateway transactionGateway,
        FindTransaction findTransaction,
        FindAccount findAccount,
        UpdateAccount updateAccount
) {

    public void delete(final Long id) {
        log.info("[deleteAccount:{}] Deleting a account", id);
        deleteValidation(id);
        transactionGateway.delete(id);
    }

    private void deleteValidation(Long id) {
        final var byId = findTransaction.findById(id);

        if (byId.getPaid()) {
            if (byId.getTransactionType() == TransactionType.REVENUE) {
                final Long id1 = byId.getAccount().getId();
                final var accountEntity = findAccount.byId(id1);
                accountEntity.setAmount(accountEntity.getAmount().subtract(byId.getAmount()));
                updateAccount.update(accountEntity);
            }

            if (byId.getTransactionType() == TransactionType.EXPENSE) {
                final Long id1 = byId.getAccount().getId();
                final var accountEntity = findAccount.byId(id1);
                accountEntity.setAmount(accountEntity.getAmount().add(byId.getAmount()));
                updateAccount.update(accountEntity);
            }
        }
    }
}
