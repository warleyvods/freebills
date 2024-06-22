package com.freebills.gateways;

import com.freebills.domain.Category;
import com.freebills.exceptions.CategoryNotFoundException;
import com.freebills.gateways.entities.CategoryEntity;
import com.freebills.gateways.mapper.CategoryGatewayMapper;
import com.freebills.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Category> findAll(final String username) {
        return categoryRepository.findAllCategoryByUser(username)
                .stream()
                .sorted(Comparator.comparing(CategoryEntity::getId))
                .map(categoryGatewayMapper::toDomain)
                .toList();
    }

    public Category findById(final Long id, final String username) {
        return categoryGatewayMapper.toDomain(categoryRepository.findByIdWithUser(id, username)
                .orElseThrow(() -> new CategoryNotFoundException("Category id: " + id + " not found!")));
    }

    public Category update(final Category category) {
        return categoryGatewayMapper.toDomain(categoryRepository.save(categoryGatewayMapper.toEntity(category)));
    }

    public void deleteById(final Long id, final String username) {
        categoryRepository.deleteCategoryEntityByIdAndUser_Login(id, username);
    }
}
