package com.freebills.gateways;

import com.freebills.domain.Account;
import com.freebills.exceptions.AccountNotFoundException;
import com.freebills.gateways.mapper.AccountGatewayMapper;
import com.freebills.repositories.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountGateway {

    private final AccountsRepository accountsRepository;
    private final AccountGatewayMapper accountGatewayMapper;

    public Account save(final Account account) {
        return accountGatewayMapper.toDomain(accountsRepository.save(accountGatewayMapper.toEntity(account)));
    }

    public List<Account> findByUserLogin(final String login) {
        return accountsRepository.findByUser_Login(login).stream().map(accountGatewayMapper::toDomain).toList();
    }

    public Account findById(final Long id) {
        return accountGatewayMapper.toDomain(accountsRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found!: " + id)));
    }

    public Account update(final Account account) {
        return accountGatewayMapper.toDomain(accountsRepository.save(accountGatewayMapper.toEntity(account)));
    }

    public void deleteById(final Long id) {
        accountsRepository.deleteById(id);
    }
}
