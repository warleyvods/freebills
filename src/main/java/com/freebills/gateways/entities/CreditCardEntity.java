package com.freebills.gateways.entities;

import com.freebills.gateways.entities.enums.CardFlag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "credit_card")
@EntityListeners(AuditingEntityListener.class)
public class CreditCardEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private BigDecimal cardLimit;
    private String description;

    private Integer expirationDay;
    private Integer closingDay;
    private Boolean archived;

    @Enumerated(STRING)
    private CardFlag cardFlag;

    @ManyToOne
    private AccountEntity account;

    @OneToMany(mappedBy = "creditCard")
    private List<CCTransactionEntity> ccTransactionEntities = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
