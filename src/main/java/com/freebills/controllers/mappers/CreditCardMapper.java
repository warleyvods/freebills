package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.CreditCardPostRequestDTO;
import com.freebills.controllers.dtos.requests.CreditCardPutRequestDTO;
import com.freebills.controllers.dtos.responses.CreditCardResponseDTO;
import com.freebills.domain.CreditCard;
import com.freebills.gateways.AccountGateway;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccountGateway.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditCardMapper {

    @Mapping(source = "accountId", target = "account")
    @Mapping(target = "archived", constant = "false")
    CreditCard toDomain(CreditCardPostRequestDTO creditCardPostRequestDTO);

    CreditCardResponseDTO fromDomain(CreditCard creditCard);

    List<CreditCardResponseDTO> fromDomainList(List<CreditCard> creditCards);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CreditCard toDomain(CreditCardPutRequestDTO creditCardPutRequestDTO, @MappingTarget CreditCard creditCard);

    default CreditCard toggleArchived(@MappingTarget CreditCard creditCard) {
        creditCard.setArchived(!creditCard.getArchived());
        return creditCard;
    }
}