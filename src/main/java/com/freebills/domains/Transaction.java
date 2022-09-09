package com.freebills.domains;


import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
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

}
