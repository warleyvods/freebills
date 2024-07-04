package com.freebills.gateways.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "transfers")
@EntityListeners(AuditingEntityListener.class)
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String observation;
    private String description;
    private String transferCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private AccountEntity from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private AccountEntity to;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
