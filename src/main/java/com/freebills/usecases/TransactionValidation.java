package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

@Component
public record TransactionValidation(AccountGateway accountGateway) {

    public void transactionValidation(Transaction transaction) {
        if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
            final Account account = accountGateway.findById(transaction.getAccount().getId());
            account.setAmount(account.getAmount().subtract(transaction.getAmount()));
            accountGateway.save(account);
        }

        if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
            final Account account = accountGateway.findById(transaction.getAccount().getId());
            account.setAmount(account.getAmount().add(transaction.getAmount()));
            accountGateway.save(account);
        }
    }
}
