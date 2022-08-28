package com.freebills.usecases;

import com.freebills.domains.User;
import com.freebills.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public record InsertAdminUser(UserRepository userRepository) {

    private static final String ADMIN = "admin";

    public void insertAdminUser() {
        if (userRepository.findByLoginIgnoreCase(ADMIN).isEmpty()) {
            log.debug("Administrator user not found, creating...");
            final var user = new User();
            user.setName("Administrator");
            user.setLogin(ADMIN);
            user.setAdmin(true);
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode("baguvix"));
            user.setEmail("email@email.com");
            userRepository.save(user);
        } else {
            log.info("insertAdminUser: admin user already exists");
        }
    }
}
