package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.AccountPatchArchivedRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.domain.Account;
import com.freebills.domain.User;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.UserGateway;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(componentModel = "spring", uses = UserGateway.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AccountMapper {

    @Autowired
    protected UserGateway userGateway;

    @Mapping(source = "userLogged", target = "user", qualifiedByName = "mapUserByLogin")
    public abstract Account toDomain(AccountPostRequestDTO accountPostRequestDTO, String userLogged);

    public abstract AccountResponseDTO toDTO(Account account);

    public abstract List<Account> fromDomainList(List<AccountEntity> accountEntities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Account updateAccountFromDTO(AccountPutRequestDTO accountPutRequestDTO, @MappingTarget Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Account updateArchiveAccountFromDTO(AccountPatchArchivedRequestDTO accDTO, @MappingTarget Account account);

    @Named("mapUserByLogin")
    public User mapUserByLogin(String login) {
        return userGateway.findByLogin(login);
    }
}