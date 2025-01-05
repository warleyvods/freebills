package com.freebills.gateways.entities;


import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Setter
@Getter
@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotBlank
    @Size(max = 255)
    private String description;

    @Size(max = 50)
    private String barCode;

    private Boolean bankSlip;

    @Enumerated(STRING)
    private TransactionType transactionType;

    @Enumerated(STRING)
    private TransactionCategory transactionCategory;

    private Boolean paid;

    @ManyToOne
    private AccountEntity account;

    @ManyToOne
    private CategoryEntity category;

    private String observation;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
