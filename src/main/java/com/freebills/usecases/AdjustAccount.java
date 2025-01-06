package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.freebills.gateways.entities.enums.TransactionType.ADJUST;
import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;
import static java.time.LocalDate.now;

@Component
@RequiredArgsConstructor
public class AdjustAccount {

    private final FindAccount findAccount;
    private final FindCategory findCategory;
    private final CreateTransaction createTransaction;

    @Transactional
    @CacheEvict(value = {"transaction", "account"}, key = "#id")
    public void execute(final Long id, final BigDecimal amount, final String username) {
        final Account account = findAccount.byId(id);

        if (account.getAmount().compareTo(amount) == 0) {
            return;
        }

        final var category = findCategory.findByCategoryType(ADJUST, username);

        BigDecimal difference = account.getAmount().subtract(amount);
        TransactionType transactionType;

        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            transactionType = EXPENSE;
        } else {
            transactionType = REVENUE;
            difference = difference.abs();
        }

        //REMOVE THIS METHOD
    }
}
