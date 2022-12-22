package com.freebills.gateways;

import com.freebills.domains.Account;
import com.freebills.exceptions.AccountNotFoundException;
import com.freebills.repositories.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountGateway {

    private final AccountsRepository accountsRepository;

    public Account save(final Account account) {
        return accountsRepository.save(account);
    }

    public List<Account> findByUserLogin(final String login) {
        return accountsRepository.findByUser_Login(login);
    }

    public Account findById(final Long id) {
        return accountsRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found!"));
    }

    public Account update(final Account account) {
        return accountsRepository.save(account);
    }

    public void deleteById(final Long id) {
        accountsRepository.deleteById(id);
    }
}
