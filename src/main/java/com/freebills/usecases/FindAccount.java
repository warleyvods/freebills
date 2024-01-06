package com.freebills.usecases;


import com.freebills.entities.Account;
import com.freebills.gateways.AccountGateway;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public record FindAccount(AccountGateway accountGateway) {

    public Account byId(final Long id){
        return accountGateway.findById(id);
    }

    public List<Account> findByAccountsNonArchived(final String login) {
        return accountGateway.findByUserLogin(login)
                .stream()
                .filter(acc -> !acc.isArchived())
                .sorted(Comparator.comparing(Account::getDescription))
                .toList();
    }

    public List<Account> findByAccountsArchived(final String login){
        return accountGateway.findByUserLogin(login).stream().filter(Account::isArchived).toList();
    }
}
