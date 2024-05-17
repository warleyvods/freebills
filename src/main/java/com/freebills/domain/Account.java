package com.freebills.domain;

import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class Account {

    private Long id;
    private BigDecimal amount;
    private String description;
    private AccountType accountType;
    private BankType bankType;
    private Boolean archived;
    private Boolean dashboard;
    private User user;

    public Boolean isArchived() {
        return archived;
    }
}
