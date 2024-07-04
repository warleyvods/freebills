package com.freebills.gateways.mapper;

import com.freebills.domain.Transfer;
import com.freebills.gateways.entities.TransferEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TransferGatewayMapper {

    Transfer toDomain(TransferEntity transfer);

    TransferEntity toEntity(Transfer transfer);

}
