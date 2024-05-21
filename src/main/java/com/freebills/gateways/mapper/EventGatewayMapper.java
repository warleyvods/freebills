package com.freebills.gateways.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.EventEntity;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EventGatewayMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "transactionData", ignore = true)
    @Mapping(target = "oldTransactionData", ignore = true)
    public abstract Event toDomain(EventEntity eventEntity);

    @Mapping(target = "transactionData", ignore = true)
    @Mapping(target = "oldTransactionData", ignore = true)
    public abstract EventEntity toEntity(Event event);

    public EventEntity toEntityWithJson(Event event) {
        EventEntity entity = toEntity(event);
        if (event.getTransactionData() != null) {
            try {
                entity.setTransactionData(objectMapper.writeValueAsString(event.getTransactionData()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        if (event.getOldTransactionData() != null) {
            try {
                entity.setOldTransactionData(objectMapper.writeValueAsString(event.getOldTransactionData()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return entity;
    }

    public Event toDomainWithJson(EventEntity eventEntity) {
        Event event = toDomain(eventEntity);
        if (eventEntity.getTransactionData() != null) {
            try {
                event.setTransactionData(objectMapper.readValue(eventEntity.getTransactionData(), Transaction.class));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        if (eventEntity.getOldTransactionData() != null) {
            try {
                event.setOldTransactionData(objectMapper.readValue(eventEntity.getOldTransactionData(), Transaction.class));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return event;
    }
}
