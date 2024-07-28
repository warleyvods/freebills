package com.freebills.usecases;

import com.freebills.domain.Category;
import com.freebills.events.category.CreateCategoryEvent;
import com.freebills.gateways.CategoryGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.freebills.gateways.entities.enums.TransactionType.UNKNOW;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindCategory {

    private final CategoryGateway categoryGateway;
    private final FindUser findUser;
    private final ApplicationEventPublisher eventPublisher;

    public Category findById(final Long id, final String username) {
        return categoryGateway.findById(id, username);
    }

    public Page<Category> findAll(final String username,
                                  final String keyword,
                                  final String categoryType,
                                  final Boolean active,
                                  final Pageable pageable) {
        return categoryGateway.findAll(username, keyword, categoryType, active, pageable);
    }

    public Category findById(final Long id) {
        final String loggedInUsername = getLoggedInUsername();

        if (id == null) {
            return categoryGateway.findByCategoryType(UNKNOW, loggedInUsername);
        }

        return categoryGateway.findById(id);
    }

    public Category findByCategoryType(final TransactionType type, final String username) {
        return categoryGateway.findByCategoryType(type, username);
    }

    public boolean existByCategoryType(TransactionType transactionType, String login) {
        return categoryGateway.existsByCategoryType(transactionType, login);
    }

    public Category findByCategoryByNameOrCreateCategory(final String name, final TransactionType transactionType) {
        final var loggedInUsername = getLoggedInUsername();
        final var user = findUser.byLoginOrEmail(loggedInUsername);

        final var existingCategory = categoryGateway.findByCategoryName(name, transactionType.name(), loggedInUsername);

        if (existingCategory.isPresent()) {
            log.info("[find-category: {}] category found", name);
            return existingCategory.get();
        }

        final Category category = new Category(
                name,
                "data:image/svg+xml;utf8,<svg fill='%23222222' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'><path d='M256 32c14.2 0 27.3 7.5 34.5 19.8l216 368c7.3 12.4 7.3 27.7 .2 40.1S486.3 480 472 480H40c-14.3 0-27.6-7.7-34.7-20.1s-7-27.8 .2-40.1l216-368C228.7 39.5 241.8 32 256 32zm0 128c-13.3 0-24 10.7-24 24V296c0 13.3 10.7 24 24 24s24-10.7 24-24V184c0-13.3-10.7-24-24-24zm32 224a32 32 0 1 0 -64 0 32 32 0 1 0 64 0z'/></svg>",
                "#000000",
                transactionType,
                false,
                user
        );

        log.info("[find-category: {}] category not found, creating new category", name);

        final var futureCategory = new CompletableFuture<Category>();

        eventPublisher.publishEvent(new CreateCategoryEvent(this, category, futureCategory));
        return futureCategory.join();
    }

    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal != null) {
            return principal.toString();
        } else {
            throw new IllegalStateException("No principal found in security context");
        }
    }
}
