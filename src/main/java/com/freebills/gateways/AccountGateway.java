package com.freebills.gateways;

import com.freebills.domains.Account;
import com.freebills.repositories.AccountsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record AccountGateway(AccountsRepository accountsRepository) {

    public Account save(final Account account) {
        return accountsRepository.save(account);
    }

    public List<Account> findAll() {
        return accountsRepository.findAll();
    }

    public Account findById(final Long id) {
        return accountsRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));
    }

    public Account update(final Account account) {
        return accountsRepository.save(account);
    }

    public void deleteById(final Long id) {
        accountsRepository.deleteById(id);
    }
}
