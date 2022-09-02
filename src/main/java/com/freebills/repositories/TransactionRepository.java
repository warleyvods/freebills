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
            " AND EXTRACT(MONTH FROM date) = :month " +
            " AND EXTRACT(YEAR FROM date) = :year",
            nativeQuery = true)
    Page<Transaction> findByTransactionFilterByDate(
            @Param(value = "userId") final Long userId,
            @Param(value = "month") final Integer month,
            @Param(value = "year") final Integer year,
            final Pageable pageable);

}
