package com.freebills.repositories;

import com.freebills.gateways.entities.CCTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CCTransactionsRepository extends JpaRepository<CCTransactionEntity, Long> {

    @Query(value = """
            SELECT cct FROM CCTransactionEntity cct
            INNER JOIN cct.creditCard cc
            INNER JOIN cc.account ac
            INNER JOIN ac.user u
            WHERE u.login = :username
            AND cc.id = :cardId
            AND (:month IS NULL OR MONTH(cct.date) = :month)
            AND (:year IS NULL OR YEAR(cct.date) = :year)
            AND (:keyword IS NULL OR CAST(cct.description AS text) ILIKE CAST(CONCAT('%', :keyword, '%') AS text))
            """)
    Page<CCTransactionEntity> findAllWithFilters(
            final Long cardId,
            final String username,
            final Integer month,
            final Integer year,
            final String keyword,
            final Pageable pageable
    );

    @Query(value = """
            SELECT cct FROM CCTransactionEntity cct
            INNER JOIN cct.creditCard cc
            INNER JOIN cc.account ac
            INNER JOIN ac.user u
            WHERE u.login = :username
            AND cct.id = :ccTransactionId
            """)
    Optional<CCTransactionEntity> findByIdAndUsername(Long ccTransactionId, String username);
}
