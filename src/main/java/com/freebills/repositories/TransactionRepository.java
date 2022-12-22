package com.freebills.repositories;

import com.freebills.domains.Transaction;
import com.freebills.domains.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(" SELECT t1 FROM Transaction t1 " +
            " INNER JOIN t1.account ac " +
            " INNER JOIN ac.user u " +
            " WHERE u.login = :login " +
            " AND MONTH(t1.date) = COALESCE(:month, MONTH(t1.date))" +
            " AND YEAR(t1.date) = COALESCE(:year, YEAR(t1.date)) " +
            " AND t1.description LIKE concat('%', COALESCE(:keyword, t1.description), '%') " +
            " AND t1.transactionType = COALESCE(:transactionType, t1.transactionType) ")
    Page<Transaction> findByTransactionFilterByDate(
            @Param(value = "login") final String login,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "keyword") final String keyword,
            @Param(value = "transactionType") final TransactionType transactionType,
            final Pageable pageable);
}
