package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateTransaction(TransactionGateway transactionGateway, AccountGateway accountGateway) {

    public Transaction update(final Transaction transaction) {
        final Transaction transactionSaved = transactionGateway.update(transaction);

        if (transactionSaved.isPaid()) {
            final Account account = accountGateway.findById(transactionSaved.getAccount().getId());
            account.setAmount(account.getAmount().subtract(transactionSaved.getAmount()));
            accountGateway.save(account);
        }

        return transactionSaved;
    }
}
