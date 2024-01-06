package com.freebills.controllers.mappers;


import com.freebills.annotations.EncodedMapping;
import com.freebills.controllers.dtos.requests.UserPostRequestDTO;
import com.freebills.controllers.dtos.requests.SignupUserRequestDTO;
import com.freebills.controllers.dtos.requests.UserPutPasswordRequestDTO;
import com.freebills.controllers.dtos.requests.UserPutRequestDTO;
import com.freebills.controllers.dtos.responses.UserResponseDTO;
import com.freebills.gateways.entities.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    UserEntity toDomain(UserPostRequestDTO userPostRequestDTO);

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    UserEntity toDomainUser(SignupUserRequestDTO signupUserRequestDTO);

    UserResponseDTO fromDomain(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity updateUserFromDTO(UserPutRequestDTO userPutRequestDTO, @MappingTarget UserEntity userEntity);

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    UserEntity updatePasswordFromDTO(UserPutPasswordRequestDTO userPutPasswordRequestDTO, @MappingTarget UserEntity userEntity);

}
