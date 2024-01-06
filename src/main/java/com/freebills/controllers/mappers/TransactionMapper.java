package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequesDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.entities.Transaction;
import com.freebills.gateways.AccountGateway;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = AccountGateway.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(source = "accountId", target = "account")
    Transaction toDomain(TransactionPostRequestDTO transactionPostRequestDto);

    @Mapping(source = "account.id", target = "accountId")
    TransactionResponseDTO fromDomain(Transaction transaction);

    @Mapping(source = "accountId", target = "account")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction updateTransaction(TransactionPutRequesDTO transactionPutRequesDTO, @MappingTarget Transaction transaction);

    default Transaction updateTransactionFromDto(TransactionPutRequesDTO transactionPutRequesDTO, Transaction transaction) {
        if (transaction.getAccount() != null && !transaction.getAccount().getId().equals(transactionPutRequesDTO.accountId())) {
            transaction.setFromAccount(transaction.getAccount().getId());
            transaction.setToAccount(transactionPutRequesDTO.accountId());
            transaction.setTransactionChange(true);
            return updateTransaction(transactionPutRequesDTO, transaction);
        }

        if (transaction.getAmount().compareTo(BigDecimal.valueOf(transactionPutRequesDTO.amount())) != 0 || transaction.getPreviousAmount() == null) {
            transaction.setPreviousAmount(transaction.getAmount());
        }

        transaction.setTransactionChange(false);
        return updateTransaction(transactionPutRequesDTO, transaction);
    }
}
