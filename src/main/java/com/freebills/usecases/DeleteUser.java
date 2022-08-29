package com.freebills.usecases;

import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.gateways.UserGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteUser(UserGateway userService, FindUser findUser) {

    private static final String ADMIN = "admin";

    public void byId(final Long id) {
        deleteValidation(id);
        userService.deleteById(id);
        log.info("[deleteUser][userId deleted: {}]", id);
    }

    private void deleteValidation(Long id) {
        final var user = findUser.byId(id);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (user.getLogin().equalsIgnoreCase(ADMIN)) {
            throw new PermissionDeniedException("You cannot delete a developer user");
        } else if (authentication.getName().equalsIgnoreCase(user.getLogin())) {
            throw new PermissionDeniedException("You cannot delete your own user");
        }
    }
}
