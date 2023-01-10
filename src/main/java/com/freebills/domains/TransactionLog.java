package com.freebills.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private Long inicialAccount;
    private Long previousAccount;
    private Long atualAccount;
    private BigDecimal atualAmount;

    @ManyToOne
    private Transaction transaction;

    public TransactionLog(BigDecimal inicialAmount,
                          BigDecimal previousAmount,
                          Long inicialAccount,
                          Long previousAccount,
                          Long atualAccount,
                          BigDecimal atualAmount,
                          Transaction transaction) {
        this.inicialAmount = inicialAmount;
        this.previousAmount = previousAmount;
        this.inicialAccount = inicialAccount;
        this.previousAccount = previousAccount;
        this.atualAccount = atualAccount;
        this.atualAmount = atualAmount;
        this.transaction = transaction;
    }

    public TransactionLog() {
    }
}
