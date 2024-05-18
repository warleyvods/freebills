package com.freebills.domain;

import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Event {

    private Long id;
    private Long aggregateId;
    private EventType eventType;
    private BigDecimal transactionAmount;
    private BigDecimal oldTransactionAmount;
    private BigDecimal newTransactionAmount;
    private TransactionType transactionType;
    private TransactionType oldTransactionType;
    private TransactionType newTransactionType;
    private Boolean paid;
    private Boolean oldPaid;
    private Boolean newPaid;

}
