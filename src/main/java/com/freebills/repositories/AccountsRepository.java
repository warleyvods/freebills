package com.freebills.repositories;

import com.freebills.gateways.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByUser_Login(final String login);

}
