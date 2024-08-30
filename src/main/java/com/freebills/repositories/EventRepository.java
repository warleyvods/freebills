package com.freebills.repositories;

import com.freebills.gateways.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query(value = """
        select e.* from events e order by e.created_at
    """, nativeQuery = true)
    List<EventEntity> findByAggregateId(Long aggregateId);
}