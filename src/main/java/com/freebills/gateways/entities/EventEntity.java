package com.freebills.gateways.entities;

import com.freebills.gateways.entities.enums.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long aggregateId;

    @Enumerated(STRING)
    private EventType eventType;

    @Column(columnDefinition = "TEXT")
    private String transactionData;

    @Column(columnDefinition = "TEXT")
    private String oldTransactionData;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}