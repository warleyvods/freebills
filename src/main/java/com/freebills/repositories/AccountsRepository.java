package com.freebills.repositories;

import com.freebills.gateways.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser_Login(final String login);

}
