package com.freebills.usecases;

import com.freebills.entities.Account;
import com.freebills.entities.Transaction;
import com.freebills.entities.enums.TransactionCategory;
import com.freebills.entities.enums.TransactionType;
import com.freebills.gateways.AccountGateway;
import com.freebills.repositories.TransactionLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class TransactionValidation {

    private final AccountGateway accountGateway;
    private final TransactionLogRepository transactionLogRepository;

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
                    acc.setAmount(acc.getAmount().add(transaction.getAmount()));
                    accountGateway.save(acc);

                    final Account account = accountGateway.findById(transaction.getAccount().getId());
                    account.setAmount(acc.getAmount().subtract(transaction.getAmount()));
                    accountGateway.save(account);
                    return;
                }

                final Account account = accountGateway.findById(transaction.getAccount().getId());
                final var transactionLog = transactionLogRepository.findTransactionLogByTransaction_Id(transaction.getId()).stream().reduce((a, b) -> b).orElse(null);

                assert transactionLog != null;
                if (transactionLog.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().subtract(difference));
                } else if (transactionLog.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {

                    if (transactionLog.getPreviousTransactionType() != transaction.getTransactionType()) {
                        account.setAmount(account.getAmount().subtract(transaction.getAmount().multiply(new BigDecimal(2))));
                    } else {
                        account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                    }
                } else {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount()).multiply(BigDecimal.valueOf(-1));
                    account.setAmount(account.getAmount().add(difference));
                }

                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
                if (transaction.getFromAccount() != null && transaction.isTransactionChange()) {
                    return;
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
                final var transactionLog = transactionLogRepository.findTransactionLogByTransaction_Id(transaction.getId()).stream().reduce((a, b) -> b).orElse(null);

                assert transactionLog != null;
                if (transactionLog.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transaction.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().subtract(difference));
                } else if (transactionLog.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {
                    //trocando tipo
                    if (transactionLog.getPreviousTransactionType() != transaction.getTransactionType()) {
                        account.setAmount(account.getAmount().add(transaction.getAmount().multiply(new BigDecimal(2))));
                    } else {
                        account.setAmount(account.getAmount().add(transaction.getAmount()));
                    }
                } else {
                    final var difference = transactionLog.getPreviousAmount().subtract(transaction.getAmount()).multiply(BigDecimal.valueOf(-1));
                    account.setAmount(account.getAmount().add(difference));
                }

                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
                if (transaction.getFromAccount() != null && transaction.isTransactionChange()) {
                    return;
                }

                final var transactionLog = transactionLogRepository.findTransactionLogByTransaction_Id(transaction.getId()).stream().reduce((a, b) -> b).orElse(null);
                final Account account = accountGateway.findById(transaction.getAccount().getId());

                assert transactionLog != null;
                if (transactionLog.getPreviousAmount().compareTo(transaction.getAmount()) > 0) {
                    final var difference = transactionLog.getPreviousAmount().subtract(transaction.getAmount());
                    account.setAmount(account.getAmount().add(difference));
                } else if (transactionLog.getPreviousAmount().compareTo(transaction.getAmount()) == 0) {
                    account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                } else {
                    account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                }

                accountGateway.save(account);
            }
        }
    }
}
