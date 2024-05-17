package com.freebills.gateways.entities;


import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Transaction {

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

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionCategory transactionCategory;

    private boolean paid;

    private Long fromAccount;
    private Long toAccount;
    private boolean transactionChange;
    private BigDecimal previousAmount;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionLog> transactionLogs;

    @ManyToOne
    private AccountEntity account;

    public Transaction(BigDecimal amount,
                       LocalDate date,
                       String description,
                       TransactionType transactionType,
                       TransactionCategory transactionCategory,
                       boolean paid,
                       AccountEntity accountEntity) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.paid = paid;
        this.account = accountEntity;
    }
}
