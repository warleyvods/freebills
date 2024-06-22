package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUser {

    private final UserGateway userGateway;
    private final CreateCategory createCategory;

    private List<String> defaultCategories() {
        final var categories = new ArrayList<String>();
        categories.add("HOUSE");
        categories.add("EDUCATION");
        categories.add("ELECTRONIC");
        categories.add("LEISURE");
        categories.add("RESTAURANT");
        categories.add("HEALTH");
        categories.add("SERVICE");
        categories.add("SUPERMARKET");
        categories.add("TRANSPORT");
        categories.add("CLOTHES");
        categories.add("TRIPS");
        categories.add("OTHERS");
        categories.add("AWARD");
        categories.add("GIFT");
        categories.add("SALARY");
        categories.add("REAJUST");

        return categories;
    }

    public User execute(final User user) {
        log.info("[create-user: {}] creating new user", user.getLogin());
        return userGateway.save(user);
    }

    private void validateCategories(final User user) {
//        if (user.getCategories().isEmpty()) {
//            defaultCategories().forEach(categoryName -> createCategory.execute(Category.builder().name(categoryName).build()));
//        }
    }
}
