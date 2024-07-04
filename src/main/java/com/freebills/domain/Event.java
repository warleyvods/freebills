package com.freebills.domain;

import com.freebills.gateways.entities.enums.EventType;
import com.freebills.gateways.entities.enums.TransferType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Event {

    private Long id;
    private Long aggregateId;
    private EventType eventType;
    private BigDecimal amount;
    private TransferType transferType;
    private Transaction transactionData;
    private Transaction oldTransactionData;
    private LocalDateTime createdAt;

}
