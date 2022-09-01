package com.freebills.domains;


import com.freebills.domains.enums.TransactionCategory;
import com.freebills.domains.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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
    private boolean paid;

    private LocalDate date;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(value = EnumType.STRING)
    private TransactionCategory transactionCategory;

    @ManyToOne
    private Account account;
}
