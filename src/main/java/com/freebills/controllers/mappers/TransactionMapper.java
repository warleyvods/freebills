package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequesDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.domains.Transaction;
import com.freebills.gateways.AccountGateway;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountGateway.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(source = "accountId", target = "account")
    Transaction toDomain(TransactionPostRequestDTO transactionPostRequestDto);

    TransactionResponseDTO fromDomain(Transaction transaction);

    List<TransactionResponseDTO> fromDomainList(List<Transaction> transactions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction updateTransactionFromDto(TransactionPutRequesDTO transactionPutRequesDTO, @MappingTarget Transaction transaction);

}
