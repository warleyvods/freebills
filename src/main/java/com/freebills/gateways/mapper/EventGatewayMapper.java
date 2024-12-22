package com.freebills.gateways.mapper;

import com.freebills.domain.Account;
import com.freebills.domain.Event;
import com.freebills.gateways.entities.EventEntity;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Slf4j
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EventGatewayMapper {

    @Mapping(target = "transferData", source = "transferJsonData")
    @Mapping(target = "oldTransferData", source = "oldTransferJsonData")
    public abstract Event toDomain(EventEntity eventEntity);

    @Mapping(target = "transferJsonData", source = "transferData")
    @Mapping(target = "oldTransferJsonData", source = "oldTransferData")
    public abstract EventEntity toEntity(Event event);

    protected Account map(Long id) {
        return id != null ? new Account(id) : null;
    }

    protected Long map(Account account) {
        return account != null ? account.getId() : null;
    }
}
