package com.freebills.domains;


import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String barcode;

    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(value = EnumType.STRING)
    private TransactionCategory transactionCategory;

    private boolean paid;

    @ManyToOne
    private Account account;

    public Transaction(BigDecimal amount,
                       LocalDate date,
                       String description,
                       TransactionType transactionType,
                       TransactionCategory transactionCategory,
                       boolean paid,
                       Account account) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.paid = paid;
        this.account = account;
    }
}
