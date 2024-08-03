package com.freebills.usecases;

import com.freebills.gateways.CategoryGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.freebills.gateways.entities.enums.TransactionType.ADJUST;
import static com.freebills.gateways.entities.enums.TransactionType.UNKNOW;

@Component
@RequiredArgsConstructor
public class DeleteCategory {

    private final CategoryGateway categoryGateway;
    private final FindCategory findCategory;

    @Transactional
    public void execute(final Long id, final String username) {
        final var category = findCategory.findById(id, username);

        validateCategoryType(category.getCategoryType());
        categoryGateway.deleteById(id, username);
    }

    public void validateCategoryType(final TransactionType transactionType) {
        if (transactionType == UNKNOW) {
            throw new IllegalArgumentException("you cannot delete an UNKNOW category");
        }

        if (transactionType == ADJUST) {
            throw new IllegalArgumentException("you cannot delete an ADJUST category");
        }
    }
}
