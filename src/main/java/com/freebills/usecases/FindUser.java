package com.freebills.usecases;

import com.freebills.domains.User;
import com.freebills.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public record FindUser(UserGateway userService) {

    public User byId(final Long id) {
        return userService.findById(id);
    }

    public User byLogin(final String login) {
        return userService.findByLogin(login);
    }

    public Page<User> all(Pageable pageable) {
        return userService.getAll(pageable);
    }
}
