package com.freebills.usecases;

import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteUser {

    private final UserGateway userGateway;
    private final FindUser findUser;

    private static final String ADMIN = "admin";

    public void byIds(final List<Long> ids) {
        ids.forEach(this::deleteValidation);
        userGateway.deleteByIds(ids);
    }

    public void byId(final Long id) {
        deleteValidation(id);
        userGateway.deleteById(id);
        log.info("[deleteUser][userId deleted: {}]", id);
    }

    private void deleteValidation(Long id) {
        final var user = findUser.byId(id);
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (user.getLogin().equalsIgnoreCase(ADMIN)) {
            throw new PermissionDeniedException("You cannot delete a developer user");
        } else if (authentication.getName().equalsIgnoreCase(user.getLogin())) {
            throw new PermissionDeniedException("You cannot delete your own user");
        }
    }
}
