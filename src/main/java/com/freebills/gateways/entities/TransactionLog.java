package com.freebills.gateways.entities;

import com.freebills.gateways.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal inicialAmount;
    private BigDecimal previousAmount;
    private BigDecimal atualAmount;

    private Long inicialAccount;
    private Long previousAccount;
    private Long atualAccount;

    @Enumerated(value = EnumType.STRING)
    private TransactionType previousTransactionType;
    @Enumerated(value = EnumType.STRING)
    private TransactionType actualTransactionType;

    @ManyToOne
    private TransactionEntity transaction;

    public TransactionLog(BigDecimal inicialAmount,
                          BigDecimal previousAmount,
                          Long inicialAccount,
                          Long previousAccount,
                          Long atualAccount,
                          BigDecimal atualAmount,
                          TransactionEntity transactionEntity,
                          TransactionType previousTransactionType,
                          TransactionType actualTransactionType) {
        this.inicialAmount = inicialAmount;
        this.previousAmount = previousAmount;
        this.inicialAccount = inicialAccount;
        this.previousAccount = previousAccount;
        this.atualAccount = atualAccount;
        this.atualAmount = atualAmount;
        this.transaction = transactionEntity;
        this.previousTransactionType = previousTransactionType;
        this.actualTransactionType = actualTransactionType;
    }

    public TransactionLog() {
    }
}
