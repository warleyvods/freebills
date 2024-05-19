package com.freebills.usecases;

import com.freebills.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.freebills.gateways.entities.enums.TransactionCategory.REAJUST;
import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;

@Slf4j
@Component
@RequiredArgsConstructor
public final class ReajustAccount {

    private static final String DESCRIPTION = "Reajuste";

    private final FindAccount findAccount;
    private final UpdateAccount updateAccount;
    private final CreateTransaction createTransaction;

    //TODO arrumar reajuste
    public void reajust(final Long id, final BigDecimal value, final String type) {
        final var accountEntity = findAccount.byId(id);

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
    }
}
