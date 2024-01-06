package com.freebills.usecases;

import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.UserGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record CreateUser(UserGateway userGateway) {

    public UserEntity create(final UserEntity userEntity) {
        log.info("[createUser: {}] Creating new user", userEntity.getLogin());
        return userGateway.save(userEntity);
    }
}
