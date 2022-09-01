package com.freebills.repositories;

import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount_User(final User user);


}
