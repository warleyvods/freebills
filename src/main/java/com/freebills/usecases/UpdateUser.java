package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdateUser {

    private final UserGateway userGateway;
    private final FindUser findUser;

    private static final String ADMIN = "admin";

    public User execute(final User user) {
        if (user.getId() != null && lastLogin(user.getId()).equals(ADMIN)) {
            validationLoginChange(user);
        }

        return userGateway.update(user);
    }

    public void updateUserPassword(final User user, User userFinded) {
        if (!userFinded.getPassword().equals(user.getPassword())) {
            throw new PermissionDeniedException("Incorrect password!");
        }

        if (user.getLogin().equalsIgnoreCase(ADMIN)) {
            throw new PermissionDeniedException("You cannot change a developer password");
        }
        execute(user);
    }

    public void updatePassword(final User user) {
        if (user.getLogin().equalsIgnoreCase(ADMIN)) {
            throw new PermissionDeniedException("You cannot change a developer password");
        }
        execute(user);
    }

    private void validationLoginChange(final User user) {
        if (!Objects.equals(user.getLogin(), ADMIN)) {
            throw new PermissionDeniedException("You cannot change a developer login");
        }
    }

    private String lastLogin(final Long id) {
        final var user = findUser.byId(id);
        return user.getLastLogin();
    }
}
