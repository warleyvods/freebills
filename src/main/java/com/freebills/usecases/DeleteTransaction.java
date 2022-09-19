package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.gateways.TransactionGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteTransaction(TransactionGateway transactionGateway, FindTransaction findTransaction, FindAccount findAccount, UpdateAccount updateAccount) {

    public void delete(final Long id) {
        log.info("[deleteAccount:{}] Deleting a account", id);
        find(id);
        transactionGateway.delete(id);
    }

    public void find(Long id) {
        final Transaction byId = findTransaction.findById(id);

        if (byId.isPaid()) {
            final Long id1 = byId.getAccount().getId();
            final Account account = findAccount.byId(id1);
            account.setAmount(account.getAmount().subtract(byId.getAmount()));
            updateAccount.update(account);
        }
    }
}
