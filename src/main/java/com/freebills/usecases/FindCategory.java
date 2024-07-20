package com.freebills.usecases;

import com.freebills.domain.Category;
import com.freebills.gateways.CategoryGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCategory {

    private final CategoryGateway categoryGateway;

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
        return categoryGateway.findById(id);
    }

    public Category findByCategoryType(final TransactionType type, final String username) {
        return categoryGateway.findByCategoryType(type, username);
    }

    public boolean existByCategoryType(TransactionType transactionType, String login) {
        return categoryGateway.existsByCategoryType(transactionType, login);
    }
}
