package com.freebills.repositories;

import com.freebills.gateways.entities.TransferEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

    @Query(value = """
                SELECT t.*n FROM transfers t
                JOIN accounts from_account
                    ON t.from_account_id = from_account.id
                JOIN users u
                    ON from_account.user_id = u.id
                WHERE u.login = :username
                AND t.id = :id
            """, nativeQuery = true)
    Optional<TransferEntity> findByTransferIdAndUser(final Long id, final String username);

    @Query(value = """
                FROM TransferEntity t
                JOIN AccountEntity account
                    ON t.from.id = account.id
                JOIN UserEntity u
                    ON account.user.id = u.id
                WHERE u.login = :username
                AND (:month IS NULL OR MONTH(t.date) = :month)
                AND (:year IS NULL OR YEAR(t.date) = :year)
            """)
    Page<TransferEntity> findAllByUsername(final String username,
                                           final Integer year,
                                           final Integer month,
                                           final Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
                DELETE FROM TransferEntity t
                WHERE t.id = :id
                AND t.from.id IN (
                    SELECT a.id FROM AccountEntity a
                    WHERE a.user.login = :username
                )
            """)
    void deleteByIdAndUsername(Long id, String username);

}
