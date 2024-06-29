package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequestDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.domain.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.usecases.FindCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = {AccountGateway.class, FindCategory.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "accountId", target = "account")
    Transaction toDomain(TransactionPostRequestDTO transactionPostRequestDto);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "category.id", target = "categoryId")
    TransactionResponseDTO fromDomain(Transaction transaction);

    @Mapping(source = "accountId", target = "account")
    @Mapping(source = "categoryId", target = "category")
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    Transaction updateTransaction(TransactionPutRequestDTO transactionPutRequestDTO, @MappingTarget Transaction transaction);

}
