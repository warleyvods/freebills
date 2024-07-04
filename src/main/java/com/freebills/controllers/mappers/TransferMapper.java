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

    TransferResponseDTO toDTO(Transfer transfer);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    TransferResponseDTO update(TransferPutRequestDTO transferPutRequestDTO, @MappingTarget Transfer category);

}
