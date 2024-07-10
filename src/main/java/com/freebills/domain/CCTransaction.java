package com.freebills.domain;

import com.freebills.gateways.entities.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CCTransaction implements Serializable {

    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private TransactionType transactionType;
    private Category category;
    private CreditCard creditCard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
