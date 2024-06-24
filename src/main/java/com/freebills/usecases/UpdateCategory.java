package com.freebills.usecases;

import com.freebills.domain.Category;
import com.freebills.gateways.CategoryGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCategory {

    private final CategoryGateway categoryGateway;

    @Transactional
    public Category execute(final Category category) {
        return categoryGateway.update(category);
    }
}
