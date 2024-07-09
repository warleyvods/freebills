package com.freebills.repositories;

import com.freebills.gateways.entities.CCTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CCTransactionsRepository extends JpaRepository<CCTransactionEntity, Long> {

}
