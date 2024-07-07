package com.freebills.gateways.entities;

import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String description;

    @Enumerated(STRING)
    private AccountType accountType;

    @Enumerated(STRING)
    private BankType bankType;

    @ManyToOne
    private UserEntity user;

    private Boolean archived = false;

    private Boolean dashboard;

    @OneToMany(mappedBy = "account", cascade = ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "fromAccountId", cascade = ALL, orphanRemoval = true)
    private List<TransferEntity> outgoingTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "toAccountId", cascade = ALL, orphanRemoval = true)
    private List<TransferEntity> incomingTransfers = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
