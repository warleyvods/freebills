package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.UserGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record CreateUser(UserGateway userGateway) {

    public User create(final User user) {
        log.info("[create-user: {}] creating new user", user.getLogin());
        return userGateway.save(user);
    }
}
