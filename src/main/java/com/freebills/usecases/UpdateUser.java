package com.freebills.usecases;

import com.freebills.gateways.entities.UserEntity;
import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.gateways.UserGateway;
import org.springframework.stereotype.Component;

@Component
public record UpdateUser(UserGateway userGateway) {

    private static final String ADMIN = "admin";

    public UserEntity update(final UserEntity userEntity) {
        if (userEntity.getLogin().equalsIgnoreCase(ADMIN)) {
            throw new PermissionDeniedException("You cannot change any developer attributes");
        }
        return userGateway.update(userEntity);
    }

    public void updatePassword(final UserEntity userEntity) {
        if (userEntity.getLogin().equalsIgnoreCase(ADMIN)) {
            throw new PermissionDeniedException("You cannot change a developer password");
        }
        update(userEntity);
    }
}
