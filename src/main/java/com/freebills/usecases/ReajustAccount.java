package com.freebills.usecases;

import com.freebills.events.transaction.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public final class ReajustAccount {

    private static final String DESCRIPTION = "Reajuste";

    private final FindAccount findAccount;
    private final UpdateAccount updateAccount;
    private final CreateTransaction createTransaction;
    private final ApplicationEventPublisher eventPublisher;

    public void execute(final Long id, final BigDecimal value, final String type) {
        final var account = findAccount.byId(id);

//        eventPublisher.publishEvent(new TransactionCreatedEvent(this, savedTransaction.getAccount().getId(), savedTransaction));
    }
}

//        if (type.equals("true")) {
//            if (value.equals(BigDecimal.ZERO)) {
//                if (accountEntity.getAmount().compareTo(BigDecimal.ZERO) != 0) {
//                    final BigDecimal difference = accountEntity.getAmount().multiply(new BigDecimal(-1));
//                    accountEntity.setAmount(accountEntity.getAmount().add(difference));
//
//                    updateAccount.update(accountEntity);
//                    createTransaction.execute(new Transaction(difference.multiply(new BigDecimal(-1)), LocalDate.now(), DESCRIPTION, EXPENSE, REAJUST, true, accountEntity));
//                }
//            } else {
//                if (value.compareTo(accountEntity.getAmount()) > 0) {
//                    final BigDecimal diff = value.subtract(accountEntity.getAmount());
//                    accountEntity.setAmount(accountEntity.getAmount().add(diff));
//                    updateAccount.update(accountEntity);
//                    createTransaction.execute(new Transaction(diff, LocalDate.now(), DESCRIPTION, REVENUE, REAJUST, true, accountEntity));
//                } else {
//                    final BigDecimal diff = accountEntity.getAmount().subtract(value);
//                    accountEntity.setAmount(accountEntity.getAmount().subtract(diff));
//                    updateAccount.update(accountEntity);
//                    createTransaction.execute(new Transaction(diff, LocalDate.now(), DESCRIPTION, EXPENSE, REAJUST, true, accountEntity));
//                }
//            }
//        } else {
//            accountEntity.setAmount(value);
//            updateAccount.update(accountEntity);
//        }
