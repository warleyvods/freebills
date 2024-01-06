package com.freebills.usecases;

import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.UserGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public record FindUser(UserGateway userService) {

    public UserEntity byId(final Long id) {
        return userService.findById(id);
    }

    public UserEntity byLogin(final String login) {
        return userService.findByLogin(login);
    }

    public Page<UserEntity> all(Pageable pageable) {
        return userService.getAll(pageable);
    }
}
