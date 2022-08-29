package com.freebills.domains;

import com.freebills.domains.enums.AccountType;
import com.freebills.domains.enums.BankType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private boolean dashboard;

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    @ManyToOne
    private User user;

}
