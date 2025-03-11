package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionRestorePostRequestDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.controllers.dtos.responses.TransactionRestoreResponseDTO;
import com.freebills.domain.Category;
import com.freebills.domain.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.entities.enums.TransactionType;
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

    @Mapping(source = "accountId", target = "account")
    @Mapping(target = "category", expression = "java(CategoryMapperHelper.findByCategoryNameOrCreate(request.categoryName(), request.transactionType(), findCategory))")
    Transaction toDomainWithCategoryName(TransactionRestorePostRequestDTO request);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "receipt.id", target = "receiptId")
    TransactionResponseDTO fromDomain(Transaction transaction);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "category.name", target = "categoryName")
    TransactionRestoreResponseDTO fromDomainWithCategoryName(Transaction transaction);

    @Mapping(source = "accountId", target = "account")
    @Mapping(source = "categoryId", target = "category")
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    Transaction updateTransaction(TransactionPutRequestDTO transactionPutRequestDTO, @MappingTarget Transaction transaction);

    class CategoryMapperHelper {
        public static Category findByCategoryNameOrCreate(String categoryName, String transactionType, FindCategory findCategory) {
            return findCategory.findByCategoryByNameOrCreateCategory(categoryName, TransactionType.valueOf(transactionType));
        }
    }
}
