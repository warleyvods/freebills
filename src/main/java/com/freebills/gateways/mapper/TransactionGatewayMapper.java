package com.freebills.gateways.mapper;

import com.freebills.domain.Transaction;
import com.freebills.gateways.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, uses = {TransactionMetadataGatewayMapper.class})
public interface TransactionGatewayMapper {

    Transaction toDomain(TransactionEntity transactionEntity);

    TransactionEntity toEntity(Transaction transaction);

}
