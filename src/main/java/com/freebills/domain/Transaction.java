package com.freebills.domain;

import com.freebills.gateways.entities.TransactionLog;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Transaction {

    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String barCode;
    private Boolean bankSlip;
    private TransactionType transactionType;
    private TransactionCategory transactionCategory;
    private Boolean paid;
    private Long fromAccount;
    private Long toAccount;
    private Boolean transactionChange;
    private BigDecimal previousAmount;
    private List<TransactionLog> transactionLogs;
    private Account account;

    public Transaction(final BigDecimal amount,
                       final LocalDate date,
                       final String description,
                       final TransactionType transactionType,
                       final TransactionCategory transactionCategory,
                       final Boolean paid,
                       final Account account) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.paid = paid;
        this.account = account;
    }
}
