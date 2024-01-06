package com.freebills.controllers.mappers;


import com.freebills.annotations.EncodedMapping;
import com.freebills.controllers.dtos.requests.UserPostRequestDTO;
import com.freebills.controllers.dtos.requests.SignupUserRequestDTO;
import com.freebills.controllers.dtos.requests.UserPutPasswordRequestDTO;
import com.freebills.controllers.dtos.requests.UserPutRequestDTO;
import com.freebills.controllers.dtos.responses.UserResponseDTO;
import com.freebills.gateways.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User toDomain(UserPostRequestDTO userPostRequestDTO);

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User toDomainUser(SignupUserRequestDTO signupUserRequestDTO);

    UserResponseDTO fromDomain(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromDTO(UserPutRequestDTO userPutRequestDTO, @MappingTarget User user);

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User updatePasswordFromDTO(UserPutPasswordRequestDTO userPutPasswordRequestDTO, @MappingTarget User user);

}
