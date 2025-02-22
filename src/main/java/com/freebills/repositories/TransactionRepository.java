package com.freebills.repositories;

import com.freebills.gateways.entities.TransactionEntity;
import com.freebills.gateways.entities.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("""
            SELECT t1 FROM TransactionEntity t1
            INNER JOIN t1.account ac
            INNER JOIN ac.user u
            WHERE u.login = :login
            AND (:month IS NULL OR MONTH(t1.date) = :month)
            AND (:year IS NULL OR YEAR(t1.date) = :year)
            AND (:keyword IS NULL OR CAST(t1.description AS text) ILIKE CAST(CONCAT('%', :keyword, '%') AS text))
            AND (:transactionType IS NULL OR t1.transactionType = :transactionType)
            """)
    Page<TransactionEntity> findByTransactionFilterByDate(
            @Param(value = "login") final String login,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "keyword") final String keyword,
            @Param(value = "transactionType") final TransactionType transactionType,
            final Pageable pageable);


    @Query("""
            SELECT t1 FROM TransactionEntity  t1
            INNER JOIN t1.account ac
            INNER JOIN  ac.user u
            INNER JOIN t1.category cat
            WHERE u.login = :login
            AND (:category IS NULL OR t1.category.name = :category)
            AND (:month IS NULL OR MONTH(t1.date) = :month)
            AND (:year IS NULL OR YEAR(t1.date) = :year)
            AND (:transactionType IS NULL OR t1.transactionType = :transactionType)
            """)
    Page<TransactionEntity> findAllFilteredTransactionsByCategory(
            @Param(value = "login") final String login,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            @Param(value = "category") final String category,
            @Param(value = "transactionType") final TransactionType transactionType,
            final Pageable pageable
    );
}
