package com.freebills.usecases;

import com.freebills.domains.User;
import com.freebills.gateways.UserGateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Slf4j
@Component
public record CreateUser(UserGateway userGateway) {

    public User create(@Valid final User user) {
        log.info("[createUser: {}] Creating new user", user.getLogin());
        return userGateway.save(user);
    }
}
