package com.freebills.domain;

import com.freebills.gateways.entities.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Event {

    private Long id;
    private Long aggregateId;
    private EventType eventType;
    private Transaction transactionData;
    private Transaction oldTransactionData;
    private LocalDateTime createdAt;

}
