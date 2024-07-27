package com.freebills.gateways.entities;

import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.json.TransactionJsonData;
import com.freebills.gateways.entities.json.TransferJsonData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static org.hibernate.type.SqlTypes.JSON;

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

    @JdbcTypeCode(JSON)
    private TransactionJsonData transactionData;

    @JdbcTypeCode(JSON)
    private TransactionJsonData oldTransactionData;

    @JdbcTypeCode(JSON)
    private TransferJsonData transferJsonData;

    @JdbcTypeCode(JSON)
    private TransferJsonData oldTransferJsonData;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}