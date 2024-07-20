package com.freebills.gateways.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.domain.Transfer;
import com.freebills.gateways.entities.EventEntity;
import com.freebills.gateways.entities.json.TransferJsonData;
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
    @Mapping(target = "transferJsonData", ignore = true)
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

        if (event.getTransferData() != null) {
            entity.setTransferJsonData(new TransferJsonData(
                    event.getTransferData().getId(),
                    event.getTransferData().getAmount(),
                    event.getTransferData().getObservation(),
                    event.getTransferData().getDescription(),
                    event.getTransferData().getTransferType(),
                    event.getTransferData().getFromAccountId().getId(),
                    event.getTransferData().getToAccountId().getId(),
                    event.getTransferData().getUpdatedAt(),
                    event.getTransferData().getCreatedAt()
            ));
        }

        if (event.getOldTransferData() != null) {
            entity.setOldTransferJsonData(new TransferJsonData(
                    event.getOldTransferData().getId(),
                    event.getOldTransferData().getAmount(),
                    event.getOldTransferData().getObservation(),
                    event.getOldTransferData().getDescription(),
                    event.getOldTransferData().getTransferType(),
                    event.getOldTransferData().getFromAccountId().getId(),
                    event.getOldTransferData().getToAccountId().getId(),
                    event.getOldTransferData().getUpdatedAt(),
                    event.getOldTransferData().getCreatedAt()
            ));
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

        if (eventEntity.getTransferJsonData() != null) {
            final Transfer transfer = getTransfer(eventEntity);

            event.setTransferData(transfer);
        }

        if (eventEntity.getOldTransferJsonData() != null) {
            final Transfer transfer = getOldTransfer(eventEntity);

            event.setOldTransferData(transfer);
        }

        return event;
    }

    private static Transfer getTransfer(EventEntity eventEntity) {
        Transfer transfer = new Transfer();
        transfer.setId(eventEntity.getTransferJsonData().id());
        transfer.setAmount(eventEntity.getTransferJsonData().amount());
        transfer.setObservation(eventEntity.getTransferJsonData().observation());
        transfer.setDescription(eventEntity.getTransferJsonData().description());
        transfer.setTransferType(eventEntity.getTransferJsonData().transferType());
        transfer.setFromAccountId(new Account(eventEntity.getTransferJsonData().fromAccountId()));
        transfer.setToAccountId(new Account(eventEntity.getTransferJsonData().fromAccountId()));
        transfer.setUpdatedAt(eventEntity.getTransferJsonData().updatedAt());
        transfer.setCreatedAt(eventEntity.getTransferJsonData().createdAt());
        return transfer;
    }

    private static Transfer getOldTransfer(EventEntity eventEntity) {
        Transfer transfer = new Transfer();
        transfer.setId(eventEntity.getOldTransferJsonData().id());
        transfer.setAmount(eventEntity.getOldTransferJsonData().amount());
        transfer.setObservation(eventEntity.getOldTransferJsonData().observation());
        transfer.setDescription(eventEntity.getOldTransferJsonData().description());
        transfer.setTransferType(eventEntity.getOldTransferJsonData().transferType());
        transfer.setFromAccountId(new Account(eventEntity.getOldTransferJsonData().fromAccountId()));
        transfer.setToAccountId(new Account(eventEntity.getOldTransferJsonData().fromAccountId()));
        transfer.setUpdatedAt(eventEntity.getOldTransferJsonData().updatedAt());
        transfer.setCreatedAt(eventEntity.getOldTransferJsonData().createdAt());
        return transfer;
    }
}
