package com.freebills.usecases;

import com.freebills.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.FALSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class InsertAdminUser {

    @Value("${admin.password}")
    private String password;

    private final FindUser findUser;
    private final CreateUser createUser;
    private final CreateCategory createCategory;

    private static final String ADMIN = "admin";

    public void insertAdminUser() {
        if (FALSE.equals(findUser.existsByLogin(ADMIN))) {
            log.debug("Administrator user not found, creating...");

            final var user = new User();
            user.setName("Administrator");
            user.setLogin(ADMIN);
            user.setAdmin(true);
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setEmail("email@email.com");
            final User savedUser = createUser.execute(user);

            createCategory.handleCreateCategoriesDefault(savedUser);
        } else {
            log.info("insertAdminUser: admin user already exists");
        }
    }
}
