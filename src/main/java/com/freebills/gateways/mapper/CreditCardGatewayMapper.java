package com.freebills.gateways.mapper;

import com.freebills.domain.CreditCard;
import com.freebills.gateways.entities.CreditCardEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CreditCardGatewayMapper {

    CreditCard toDomain(CreditCardEntity creditCardEntity);

    CreditCardEntity toEntity(CreditCard creditCard);

}
