package com.freebills.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transfer {

    private Long id;
    private BigDecimal amount;
    private String observation;
    private String description;
    private String transferCategory;
    private Account from;
    private Account to;
    private LocalDateTime createdAt;

}
