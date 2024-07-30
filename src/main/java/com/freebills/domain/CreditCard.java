package com.freebills.domain;

import com.freebills.gateways.entities.enums.CardFlag;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreditCard implements Serializable {

    private Long id;
    private BigDecimal cardLimit;
    private String description;
    private Integer expirationDay;
    private Integer closingDay;
    private Boolean archived;
    private CardFlag cardFlag;
    private Account account;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
