package com.freebills.gateways.entities;


import com.freebills.gateways.entities.enums.CardFlag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal cardLimit;
    private String description;

    @Enumerated(EnumType.STRING)
    private CardFlag cardFlag;

    @ManyToOne
    private Account account;
    private Integer expirationDay;
    private Integer closingDay;
    private Boolean archived;

}
