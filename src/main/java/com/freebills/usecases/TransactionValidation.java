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

import java.math.BigDecimal;
import java.math.BigInteger;

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
                if (transaction.getFromAccount() != null && transaction.isTransactionChange()) {
                    final Account acc = accountGateway.findById(transaction.getFromAccount());
                    acc.setAmount(acc.getAmount().subtract(transaction.getAmount()));
                    accountGateway.save(acc);

                    final Account account = accountGateway.findById(transaction.getAccount().getId());
                    account.setAmount(acc.getAmount().add(transaction.getAmount()));
                    accountGateway.save(account);
                    return;
                }

                final Account account = accountGateway.findById(transaction.getAccount().getId());

                if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().subtract(difference));
                } else if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {
                    account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                } else {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount()).multiply(new BigDecimal(-1));
                    account.setAmount(account.getAmount().add(difference));
                }

                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
                if (transaction.getFromAccount() != null && transaction.isTransactionChange()) {
                    final Account acc = accountGateway.findById(transaction.getFromAccount());
                    acc.setAmount(acc.getAmount().add(transaction.getAmount()));
                    accountGateway.save(acc);
                }

                final Account account = accountGateway.findById(transaction.getAccount().getId());
                if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().subtract(difference));
                } else if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {
                    account.setAmount(account.getAmount().add(transaction.getAmount()));
                } else {
                    account.setAmount(account.getAmount().add(transaction.getAmount()));
                }

                accountGateway.save(account);
            }

            if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
                if (transaction.getFromAccount() != null && transaction.isTransactionChange()) {
                    final Account acc = accountGateway.findById(transaction.getFromAccount());
                    acc.setAmount(acc.getAmount().subtract(transaction.getAmount()));
                    accountGateway.save(acc);

                    final Account account = accountGateway.findById(transaction.getAccount().getId());
                    account.setAmount(acc.getAmount().add(transaction.getAmount()));
                    accountGateway.save(account);
                    return;
                }

                final Account account = accountGateway.findById(transaction.getAccount().getId());

                if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().subtract(difference));
                } else if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {
                    account.setAmount(account.getAmount().add(transaction.getAmount()));
                } else {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount()).multiply(new BigDecimal(-1));
                    account.setAmount(account.getAmount().add(difference));
                }

                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
                if (transaction.getFromAccount() != null && transaction.isTransactionChange()) {
                    final Account acc = accountGateway.findById(transaction.getFromAccount());
                    acc.setAmount(acc.getAmount().subtract(transaction.getAmount()));
                    accountGateway.save(acc);
                }

                final Account account = accountGateway.findById(transaction.getAccount().getId());
                if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().add(difference));
                } else if (transaction.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {
                    account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                } else {
                    account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                }

                accountGateway.save(account);
            }
        }
    }
}
