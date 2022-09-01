package com.freebills.usecases;

import com.freebills.domains.Account;
import com.freebills.domains.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@Component
public record CreateTransaction(TransactionGateway transactionGateway, AccountGateway accountGateway) {

    public Transaction create(@Valid final Transaction transaction) {
        final Transaction transactionSaved = transactionGateway.save(transaction);

        if (transactionSaved.isPaid()) {
            final Account account = accountGateway.findById(transactionSaved.getAccount().getId());
            account.setAmount(account.getAmount().subtract(transactionSaved.getAmount()));
            accountGateway.save(account);
        }

        return transactionSaved;
    }
}
