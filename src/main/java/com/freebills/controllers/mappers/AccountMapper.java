package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.domains.Account;
import com.freebills.gateways.AccountGateway;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = {AccountGateway.class})
public interface AccountMapper {

    @Mapping(source = "userId",target = "user")
    Account toDomain(AccountPostRequestDTO accountPostRequestDTO);

    AccountResponseDTO fromDomain(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account updateAccountFromDto(AccountPutRequestDTO accountPutRequestDTO, @MappingTarget Account account);

}
