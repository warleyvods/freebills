package com.freebills.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Account implements Serializable {

    private Long id;
    private BigDecimal amount;
    private String description;
    private AccountType accountType;
    private BankType bankType;
    private Boolean archived;
    private Boolean dashboard;

    @JsonIgnore
    private User user;

    public Account(Long id) {
        this.id = id;
    }

    public Boolean isArchived() {
        return archived;
    }
}
