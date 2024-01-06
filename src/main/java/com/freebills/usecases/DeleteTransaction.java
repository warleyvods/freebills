package com.freebills.usecases;

import com.freebills.gateways.entities.Account;
import com.freebills.gateways.entities.Transaction;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.TransactionGateway;
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
        final Transaction byId = findTransaction.findById(id);

        if (byId.isPaid()) {
            if (byId.getTransactionType() == TransactionType.REVENUE) {
                final Long id1 = byId.getAccount().getId();
                final Account account = findAccount.byId(id1);
                account.setAmount(account.getAmount().subtract(byId.getAmount()));
                updateAccount.update(account);
            }

            if (byId.getTransactionType() == TransactionType.EXPENSE) {
                final Long id1 = byId.getAccount().getId();
                final Account account = findAccount.byId(id1);
                account.setAmount(account.getAmount().add(byId.getAmount()));
                updateAccount.update(account);
            }
        }
    }
}
