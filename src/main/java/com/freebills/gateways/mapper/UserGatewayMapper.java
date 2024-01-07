package com.freebills.gateways.mapper;

import com.freebills.controllers.mappers.PasswordEncoderMapper;
import com.freebills.domain.User;
import com.freebills.gateways.entities.UserEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class, unmappedTargetPolicy = IGNORE)
public interface UserGatewayMapper {

    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User user);

}