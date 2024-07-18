package com.freebills.domain;

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
    private Category category;
    private CreditCard creditCard;
    private LocalDate expirationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
