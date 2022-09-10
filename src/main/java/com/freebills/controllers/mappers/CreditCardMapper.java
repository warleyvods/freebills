package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.CreditCardPostRequestDTO;
import com.freebills.controllers.dtos.requests.CreditCardPutRequestDTO;
import com.freebills.controllers.dtos.responses.CreditCardResponseDTO;
import com.freebills.domains.CreditCard;
import com.freebills.gateways.AccountGateway;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountGateway.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditCardMapper {

    @Mapping(source = "accountId", target = "account")
    CreditCard toDomain(CreditCardPostRequestDTO creditCardPostRequestDTO);

    CreditCardResponseDTO fromDomain(CreditCard creditCard);

    List<CreditCardResponseDTO> fromDomainList(List<CreditCard> creditCards);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CreditCard updateCreditCardFromDto(CreditCardPutRequestDTO creditCardPutRequestDTO, @MappingTarget CreditCard creditCard);

}
