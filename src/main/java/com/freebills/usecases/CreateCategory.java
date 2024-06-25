package com.freebills.usecases;

import com.freebills.domain.Category;
import com.freebills.domain.User;
import com.freebills.gateways.CategoryGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.freebills.gateways.entities.enums.TransactionType.EXPENSE;
import static com.freebills.gateways.entities.enums.TransactionType.REVENUE;

@Component
@RequiredArgsConstructor
public class CreateCategory {

    private final CategoryGateway categoryGateway;
    private final FindCategory findCategory;
    private final Random random = new Random();

    @Transactional
    public Category execute(final Category category) {
        return categoryGateway.save(category);
    }

    @EventListener
    public void handleCreateCategoriesDefault(final User user) {
        final var categories = findCategory.findAll(user.getLogin(), null, null, null, null);

        if (categories.isEmpty()) {
            defaultCategories(user).forEach(this::execute);
        }
    }

    private String generateRandomColor() {
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }

    private List<Category> defaultCategories(final User user) {
        final List<Category> categories = new ArrayList<>();

        // despesas
        categories.add(new Category("Casa", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Educação", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Eletrônica", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Lazer", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Restaurante", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Saúde", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Serviço", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Supermercado", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Transporte", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Roupas", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Viagens", generateRandomColor(), EXPENSE, user));
        categories.add(new Category("Outros", generateRandomColor(), EXPENSE, user));

        // receitas
        categories.add(new Category("Prêmio", generateRandomColor(), REVENUE, user));
        categories.add(new Category("Presente", generateRandomColor(), REVENUE, user));
        categories.add(new Category("Salário", generateRandomColor(), REVENUE, user));

        return categories;
    }
}
