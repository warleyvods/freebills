package com.freebills.gateways.entities;

import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id", exclude = {"transactions", "cards"})
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private BigDecimal amount = new BigDecimal(0);
    private String description;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "account", cascade = ALL, fetch = LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = ALL, fetch = LAZY)
    private List<CreditCard> cards = new ArrayList<>();

    private boolean archived;
    private boolean dashboard;

}
