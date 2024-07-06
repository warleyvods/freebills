package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.TransferPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransferPutRequestDTO;
import com.freebills.controllers.dtos.responses.TransferResponseDTO;
import com.freebills.domain.Transfer;
import com.freebills.usecases.FindAccount;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = FindAccount.class, unmappedTargetPolicy = IGNORE)
public interface TransferMapper {

    @Mapping(source = "toAccountId", target = "to")
    @Mapping(source = "fromAccountId", target = "from")
    Transfer toDomain(TransferPostRequestDTO request);

    @Mapping(source = "from.id", target = "fromAccountId")
    @Mapping(source = "to.id", target = "toAccountId")
    TransferResponseDTO toDTO(Transfer transfer);

    @Mapping(source = "toAccountId", target = "to")
    @Mapping(source = "fromAccountId", target = "from")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transfer toDomain(TransferPutRequestDTO transferPutRequestDTO, @MappingTarget Transfer category);

}
