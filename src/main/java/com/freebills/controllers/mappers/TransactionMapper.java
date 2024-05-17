package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequestDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.domain.Transaction;
import com.freebills.gateways.AccountGateway;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = AccountGateway.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(source = "accountId", target = "account")
    Transaction toDomain(TransactionPostRequestDTO transactionPostRequestDto);

    @Mapping(source = "account.id", target = "accountId")
    TransactionResponseDTO fromDomain(Transaction transaction);

    @Mapping(source = "accountId", target = "account")
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    Transaction updateTransaction(TransactionPutRequestDTO transactionPutRequestDTO, @MappingTarget Transaction transaction);

    default Transaction updateTransactionFromDto(TransactionPutRequestDTO transactionPutRequestDTO, Transaction transaction) {
        if (transaction.getAccount() != null && !transaction.getAccount().getId().equals(transactionPutRequestDTO.accountId())) {
            transaction.setFromAccount(transaction.getAccount().getId());
            transaction.setToAccount(transactionPutRequestDTO.accountId());
            transaction.setTransactionChange(true);

            return updateTransaction(transactionPutRequestDTO, transaction);
        }

        if (transaction.getAmount().compareTo(BigDecimal.valueOf(transactionPutRequestDTO.amount())) != 0 || transaction.getPreviousAmount() == null) {
            transaction.setPreviousAmount(transaction.getAmount());
        }

        transaction.setTransactionChange(false);
        return updateTransaction(transactionPutRequestDTO, transaction);
    }
}
