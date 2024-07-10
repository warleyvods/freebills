package com.freebills.gateways.mapper;

import com.freebills.domain.CCTransaction;
import com.freebills.gateways.entities.CCTransactionEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CCTransactionGatewayMapper {

    CCTransaction toDomain(CCTransactionEntity ccTransactionEntity);

    CCTransactionEntity toEntity(CCTransaction ccTransaction);

}
