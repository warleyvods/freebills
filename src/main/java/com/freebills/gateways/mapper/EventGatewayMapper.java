package com.freebills.gateways.mapper;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.domain.Transaction;
import com.freebills.domain.Transfer;
import com.freebills.gateways.entities.EventEntity;
import com.freebills.gateways.entities.json.TransactionJsonData;
import com.freebills.gateways.entities.json.TransferJsonData;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Slf4j
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EventGatewayMapper {

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
            entity.setTransactionData(new TransactionJsonData(
                    event.getTransactionData().getId(),
                    event.getTransactionData().getDate(),
                    event.getTransactionData().getDescription(),
                    event.getTransactionData().getBarCode(),
                    event.getTransactionData().getBankSlip(),
                    event.getTransactionData().getTransactionType(),
                    event.getTransactionData().getTransactionCategory(),
                    event.getTransactionData().getPaid(),
                    event.getTransactionData().getAccount(),
                    event.getTransactionData().getCategory(),
                    event.getTransactionData().getCreatedAt(),
                    event.getTransactionData().getAmount()
            ));
        }

        if (event.getOldTransactionData() != null) {
            entity.setOldTransactionData(
                    new TransactionJsonData(
                            event.getOldTransactionData().getId(),
                            event.getOldTransactionData().getDate(),
                            event.getOldTransactionData().getDescription(),
                            event.getOldTransactionData().getBarCode(),
                            event.getOldTransactionData().getBankSlip(),
                            event.getOldTransactionData().getTransactionType(),
                            event.getOldTransactionData().getTransactionCategory(),
                            event.getOldTransactionData().getPaid(),
                            event.getOldTransactionData().getAccount(),
                            event.getOldTransactionData().getCategory(),
                            event.getOldTransactionData().getCreatedAt(),
                            event.getOldTransactionData().getAmount()
                    )
            );
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
            if (eventEntity.getTransactionData().id() != null) {
                event.setTransactionData(
                        new Transaction(
                                eventEntity.getTransactionData().id(),
                                eventEntity.getTransactionData().date(),
                                eventEntity.getTransactionData().description(),
                                eventEntity.getTransactionData().barCode(),
                                eventEntity.getTransactionData().bankSlip(),
                                eventEntity.getTransactionData().transactionType(),
                                eventEntity.getTransactionData().transactionCategory(),
                                eventEntity.getTransactionData().paid(),
                                eventEntity.getTransactionData().account(),
                                eventEntity.getTransactionData().category(),
                                eventEntity.getTransactionData().createdAt(),
                                eventEntity.getTransactionData().amount()
                        )
                );
            } else {
                event.setTransactionData(new Transaction(
                        null,
                        eventEntity.getTransactionData().date(),
                        eventEntity.getTransactionData().description(),
                        eventEntity.getTransactionData().barCode(),
                        eventEntity.getTransactionData().bankSlip(),
                        eventEntity.getTransactionData().transactionType(),
                        eventEntity.getTransactionData().transactionCategory(),
                        eventEntity.getTransactionData().paid(),
                        eventEntity.getTransactionData().account(),
                        eventEntity.getTransactionData().category(),
                        eventEntity.getTransactionData().createdAt(),
                        eventEntity.getTransactionData().amount()
                ));
            }
        }

        if (eventEntity.getOldTransactionData() != null) {
            if (eventEntity.getOldTransactionData().id() != null) {
                event.setOldTransactionData(
                        new Transaction(
                                eventEntity.getOldTransactionData().id(),
                                eventEntity.getOldTransactionData().date(),
                                eventEntity.getOldTransactionData().description(),
                                eventEntity.getOldTransactionData().barCode(),
                                eventEntity.getOldTransactionData().bankSlip(),
                                eventEntity.getOldTransactionData().transactionType(),
                                eventEntity.getOldTransactionData().transactionCategory(),
                                eventEntity.getOldTransactionData().paid(),
                                eventEntity.getOldTransactionData().account(),
                                eventEntity.getOldTransactionData().category(),
                                eventEntity.getOldTransactionData().createdAt(),
                                eventEntity.getOldTransactionData().amount()
                        )
                );
            } else {
                event.setOldTransactionData(new Transaction(
                        null,
                        eventEntity.getOldTransactionData().date(),
                        eventEntity.getOldTransactionData().description(),
                        eventEntity.getOldTransactionData().barCode(),
                        eventEntity.getOldTransactionData().bankSlip(),
                        eventEntity.getOldTransactionData().transactionType(),
                        eventEntity.getOldTransactionData().transactionCategory(),
                        eventEntity.getOldTransactionData().paid(),
                        eventEntity.getOldTransactionData().account(),
                        eventEntity.getOldTransactionData().category(),
                        eventEntity.getOldTransactionData().createdAt(),
                        eventEntity.getOldTransactionData().amount()
                ));
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
