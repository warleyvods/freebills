package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUser {

    private final UserGateway userGateway;
    private final ApplicationEventPublisher eventPublisher;

    public User execute(final User user) {
        log.info("[create-user: {}] creating new user", user.getLogin());

        final User save = userGateway.save(user);

        eventPublisher.publishEvent(save);
        return save;
    }
}
