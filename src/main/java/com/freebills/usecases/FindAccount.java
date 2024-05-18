package com.freebills.usecases;


import com.freebills.domain.Account;
import com.freebills.gateways.AccountGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAccount {

    private final AccountGateway accountGateway;
    private final AccountBalanceCalculator accountBalanceCalculator;

    public Account byId(final Long id) {
        Account account = accountGateway.findById(id);
        return calculateBalanceForSingleAccount(account);
    }

    public List<Account> findByAccountsNonArchived(final String login) {
        List<Account> nonArchivedAccounts = filterAndCalculateBalance(accountGateway.findByUserLogin(login), false);
        return sortAccountsByDescription(nonArchivedAccounts);
    }

    public List<Account> findByAccountsArchived(final String login) {
        return filterAndCalculateBalance(accountGateway.findByUserLogin(login), true);
    }

    private List<Account> filterAndCalculateBalance(List<Account> accounts, boolean archived) {
        return accounts.stream()
                .filter(account -> account.isArchived() == archived)
                .peek(this::calculateBalanceForSingleAccount)
                .toList();
    }

    private Account calculateBalanceForSingleAccount(Account account) {
        BigDecimal balance = accountBalanceCalculator.calculateBalanceForAccount(account);
        account.setAmount(balance);
        return account;
    }

    private List<Account> sortAccountsByDescription(List<Account> accounts) {
        return accounts.stream()
                .sorted(Comparator.comparing(Account::getDescription))
                .toList();
    }
}
