package com.freebills.gateways.mapper;

import com.freebills.domain.Account;
import com.freebills.gateways.entities.AccountEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AccountGatewayMapper {

    Account toDomain(AccountEntity accountEntity);

    AccountEntity toEntity(Account account);

}
