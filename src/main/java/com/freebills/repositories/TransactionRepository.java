package com.freebills.repositories;

import com.freebills.domains.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

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
    Page<Transaction> findByTransactionFilterByDate(
            @Param(value = "login") final String login,
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
            " AND (:keyword IS NULL OR t1.description ILIKE '%'|| :keyword ||'%') " +
            " AND t1.transaction_type = 'REVENUE' ",
            nativeQuery = true)
    Page<Transaction> findByTransactionFilterByDateRevenue(
            @Param(value = "login") final String login,
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
            " AND (:keyword IS NULL OR t1.description ILIKE '%'|| :keyword ||'%') " +
            " AND t1.transaction_type = 'EXPENSE' ",
            nativeQuery = true)
    Page<Transaction> findByTransactionFilterByDateExpense(
            @Param(value = "login") final String login,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "keyword") final String keyword,
            final Pageable pageable);
}
