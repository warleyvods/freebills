package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionValidation {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;
    private final SessionFactory sessionFactory;

    public void transactionCreationValidation(Transaction transaction) {
        if (transaction.getTransactionCategory() != TransactionCategory.REAJUST) {

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

    public void transactionUpdateValidation(Transaction transaction) {
        if (transaction.getTransactionCategory() != TransactionCategory.REAJUST) {

            if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                accountGateway.save(account);
            }

            if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                account.setAmount(account.getAmount().add(transaction.getAmount()));
                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                accountGateway.save(account);
            }
        }
    }
}
