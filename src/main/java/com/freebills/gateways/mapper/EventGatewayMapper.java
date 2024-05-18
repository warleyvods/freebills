package com.freebills.gateways.mapper;

import com.freebills.domain.Event;
import com.freebills.gateways.entities.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventGatewayMapper {

    Event toDomain(EventEntity eventEntity);

    EventEntity toEntity(Event event);

}