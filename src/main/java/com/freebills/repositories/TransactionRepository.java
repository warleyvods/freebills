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

    @Query("SELECT t1 FROM Transaction t1 " +
            " INNER JOIN t1.account ac " +
            " INNER JOIN ac.user u " +
            " WHERE u.login = :login " +
            " AND (:month IS NULL OR MONTH(t1.date) = :month) " +
            " AND (:year IS NULL OR YEAR(t1.date) = :year) " +
            " AND (:keyword IS NULL OR t1.description LIKE '%'|| :keyword ||'%')")
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
            " AND (:month IS NULL OR MONTH(t1.date) = :month) " +
            " AND (:year IS NULL OR YEAR(t1.date) = :year) " +
            " AND (:keyword IS NULL OR t1.description LIKE '%'|| :keyword ||'%') " +
            " AND t1.transaction_type = :transType ",
            nativeQuery = true)


    @Query("SELECT t1 FROM Transaction t1 " +
            " INNER JOIN t1.account ac " +
            " INNER JOIN ac.user u " +
            " WHERE u.login = :login " +
            " AND (:month IS NULL OR MONTH(t1.date) = :month) " +
            " AND (:year IS NULL OR YEAR(t1.date) = :year) " +
            " AND (:keyword IS NULL OR t1.description LIKE '%'|| :keyword ||'%')" +
            " AND t1.transaction_type = :transType "
    )
    Page<Transaction> findByUserDateFilterTransactionType(
            @Param(value = "login") final String login,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "keyword") final String keyword,
            @Param(value = "transType") final String transType,
            final Pageable pageable);
}
