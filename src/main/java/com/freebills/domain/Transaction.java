package com.freebills.domain;

import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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
    private Account account;
    private Category category;

    public Transaction(BigDecimal amount) {
        this.amount = amount;
    }
}
