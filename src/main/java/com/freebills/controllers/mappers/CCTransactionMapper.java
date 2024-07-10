package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.CCTransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.CCTransactionPutRequestDTO;
import com.freebills.domain.CCTransaction;
import com.freebills.usecases.FindCategory;
import com.freebills.usecases.FindCreditCard;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { FindCategory.class, FindCreditCard.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CCTransactionMapper {

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "creditCardId", target = "creditCard")
    CCTransaction toDomain(CCTransactionPostRequestDTO request);

//    CCTransactionResponseDTO toDTO(CCTransaction ccTransaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CCTransaction toDomain(CCTransactionPutRequestDTO request, @MappingTarget CCTransaction ccTransaction);

}
