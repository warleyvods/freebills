package com.freebills.usecases;

import com.freebills.gateways.CategoryGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCategory {

    private final CategoryGateway categoryGateway;
    private final FindCategory findCategory;

    @Transactional
    public void execute(final Long id, final String username) {
        findCategory.findById(id, username);
        categoryGateway.deleteById(id, username);
    }
}
