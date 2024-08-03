package com.freebills.usecases;

import com.freebills.domain.Category;
import com.freebills.gateways.CategoryGateway;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.usecases.strategy.transaction.TransactionDeleted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryTest {

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private FindCategory findCategory;

    @InjectMocks
    private DeleteCategory deleteCategory;

    @Test
    void shouldDeleteCategoryWhenTransactionTypeIsNotUnknowOrAdjust() {
        Category category = new Category();
        category.setCategoryType(TransactionType.EXPENSE);

        when(findCategory.findById(anyLong(), anyString())).thenReturn(category);

        deleteCategory.execute(1L, "username");

        verify(categoryGateway, times(1)).deleteById(anyLong(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenTransactionTypeIsUnknow() {
        Category category = new Category();
        category.setCategoryType(TransactionType.UNKNOW);

        when(findCategory.findById(anyLong(), anyString())).thenReturn(category);

        assertThrows(IllegalArgumentException.class, () -> deleteCategory.execute(1L, "username"));
    }

    @Test
    void shouldThrowExceptionWhenTransactionTypeIsAdjust() {
        Category category = new Category();
        category.setCategoryType(TransactionType.ADJUST);

        when(findCategory.findById(anyLong(), anyString())).thenReturn(category);

        assertThrows(IllegalArgumentException.class, () -> deleteCategory.execute(1L, "username"));
    }
}