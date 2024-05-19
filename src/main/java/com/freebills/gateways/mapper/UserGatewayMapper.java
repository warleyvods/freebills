package com.freebills.gateways.mapper;

import com.freebills.domain.User;
import com.freebills.gateways.entities.UserEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface UserGatewayMapper {

    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User user);

}