package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class FindUser {

    private final UserGateway userGateway;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public User byId(final Long id) {
        return userGateway.findById(id);
    }

    public User byLoginOrEmail(String loginOrEmail) {
        boolean isEmail = isEmail(loginOrEmail);

        if (isEmail) {
            return userGateway.findByEmail(loginOrEmail);
        } else {
            return userGateway.findByLogin(loginOrEmail);
        }
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

    public List<User> all() {
        return userGateway.findAll();
    }

    private Boolean isEmail(String username) {
        if (username == null) {
            return false;
        }

        return EMAIL_PATTERN.matcher(username).matches();
    }
}
