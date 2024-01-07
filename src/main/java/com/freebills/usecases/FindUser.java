package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.gateways.UserGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public record FindUser(UserGateway userGateway) {

    public User byId(final Long id) {
        return userGateway.findById(id);
    }

    public User byLogin(final String login) {
        return userGateway.findByLogin(login);
    }

    public Boolean existsByLogin(final String login) {
        return userGateway.existsByLogin(login);
    }

    public Boolean existsByEmail(final String email) {
        return userGateway.existsByEmail(email);
    }

    public Page<User> all(Pageable pageable) {
        return userGateway.getAll(pageable);
    }
}
