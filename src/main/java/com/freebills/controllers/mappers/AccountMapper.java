package com.freebills.controllers.mappers;


import com.freebills.controllers.dtos.requests.AccountPatchArchivedRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.gateways.entities.Account;
import com.freebills.gateways.UserGateway;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserGateway.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(source = "userId", target = "user")
    Account toDomain(AccountPostRequestDTO accountPostRequestDTO);

    AccountResponseDTO fromDomain(Account account);

    List<AccountResponseDTO> fromDomainList(List<Account> accounts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account updateAccountFromDTO(AccountPutRequestDTO accountPutRequestDTO, @MappingTarget Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account updateArchiveAccountFromDTO(AccountPatchArchivedRequestDTO accDTO, @MappingTarget Account account);

}
