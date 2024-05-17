package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.exceptions.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class VerifyPrincipal {

    private final FindUser findUser;

    public Boolean execute(final Long id, final Principal principal) {
        final User user = findUser.byLogin(principal.getName());

        if (user.getId().equals(id)) {
            return true;
        }

        throw new PermissionDeniedException("You cannot a user from this account!");
    }
}
