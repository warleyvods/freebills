package com.freebills.gateways;

import com.freebills.domain.Category;
import com.freebills.exceptions.CategoryNotFoundException;
import com.freebills.gateways.entities.CategoryEntity;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.mapper.CategoryGatewayMapper;
import com.freebills.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryGateway {

    private final CategoryRepository categoryRepository;
    private final CategoryGatewayMapper categoryGatewayMapper;

    public Category save(final Category category) {
        return categoryGatewayMapper.toDomain(categoryRepository.save(categoryGatewayMapper.toEntity(category)));
    }

    public Page<Category> findAll(final String username, final String keyword, final String categoryType, final Pageable pageable) {
        return categoryRepository.findAllCategoryByUser(username, keyword, categoryType, pageable)
                .map(categoryGatewayMapper::toDomain);
    }

    public Category findById(final Long id, final String username) {
        return categoryGatewayMapper.toDomain(categoryRepository.findByIdWithUser(id, username)
                .orElseThrow(() -> new CategoryNotFoundException("Category id: " + id + " not found!")));
    }

    public Category findById(final Long id) {
        return categoryGatewayMapper.toDomain(categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category id: " + id + " not found!")));
    }

    public Category update(final Category category) {
        return categoryGatewayMapper.toDomain(categoryRepository.save(categoryGatewayMapper.toEntity(category)));
    }

    public void deleteById(final Long id, final String username) {
        categoryRepository.deleteCategoryEntityByIdAndUser_Login(id, username);
    }
}
