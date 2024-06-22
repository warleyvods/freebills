package com.freebills.usecases;

import com.freebills.domain.Category;
import com.freebills.gateways.CategoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCategory {

    private final CategoryGateway categoryGateway;

    public Category findById(final Long id, final String username) {
        return categoryGateway.findById(id, username);
    }

    public List<Category> findAll(final String username) {
        return categoryGateway.findAll(username);
    }
}
