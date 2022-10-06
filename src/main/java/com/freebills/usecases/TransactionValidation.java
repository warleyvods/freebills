package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

//    private Transaction transactionSaved(final Long transactionId) {
//        try (Session session = sessionFactory.openSession()) {
//            return Optional.ofNullable(session.get(Transaction.class, transactionId)).orElseThrow(() -> new TransactionNotFoundException("not found!"));
//        }
//    }

    public void transactionUpdateValidation(Transaction transaction) {
        if (transaction.getTransactionCategory() != TransactionCategory.REAJUST) {

            if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.EXPENSE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                account.setAmount(account.getAmount().add(transaction.getAmount()));
                accountGateway.save(account);
            }

            if (transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
//                final Transaction foundTransaction = transactionSaved(transaction.getId());
//                if (transaction.getAmount().equals(foundTransaction.getAmount()) && transaction.isPaid() == foundTransaction.isPaid()) {
//                    System.out.println("macaco");
//                    return;
//                }

                final Account account = accountGateway.findById(transaction.getAccount().getId());
                account.setAmount(account.getAmount().add(transaction.getAmount()));
                accountGateway.save(account);
            }

            if (!transaction.isPaid() && transaction.getTransactionType() == TransactionType.REVENUE) {
                final Account account = accountGateway.findById(transaction.getAccount().getId());
                account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                accountGateway.save(account);
            }
        }
    }
}
