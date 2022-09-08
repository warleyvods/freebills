package com.freebills.repositories;

import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount_User(final User user, final Pageable pageable);

    @Query(value = "SELECT * FROM transaction t1 " +
            " INNER JOIN account ac " +
            " ON t1.account_id = ac.id " +
            " WHERE ac.user_id = :userId " +
            " AND (:month IS NULL OR EXTRACT(MONTH FROM date) = :month) " +
            " AND (:year IS NULL OR EXTRACT(YEAR FROM date) = :year) " +
            " AND (:keyword IS NULL OR t1.description ILIKE '%'|| :keyword ||'%') ",
            nativeQuery = true)
    Page<Transaction> findByTransactionFilterByDate(
            @Param(value = "userId") final Long userId,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "keyword") final String keyword,
            final Pageable pageable);

    @Query(value = "SELECT * FROM transaction t1 " +
            " INNER JOIN account ac " +
            " ON t1.account_id = ac.id " +
            " INNER JOIN \"user\" u " +
            " ON u.id = ac.user_id " +
            " WHERE u.login = :login " +
            " AND (:month IS NULL OR EXTRACT(MONTH FROM date) = :month) " +
            " AND (:year IS NULL OR EXTRACT(YEAR FROM date) = :year) " +
            " AND (:keyword IS NULL OR t1.description ILIKE '%'|| :keyword ||'%') ",
            nativeQuery = true)
    Page<Transaction> findByTransactionFilterByDate00(
            @Param(value = "login") final String login,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "keyword") final String keyword,
            final Pageable pageable);
}
