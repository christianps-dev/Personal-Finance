package org.alunosufg.personalfinancespring.categories;

import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.alunosufg.personalfinancespring.repository.CategoryRepository;
import org.alunosufg.personalfinancespring.services.CategoriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriesTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoriesService categoriesService;

    // --- Tests for addNewCategory ---

    @Test
    void addNewCategory_PositiveValue_ShouldSaveAsIncome() {
        // Arrange
        var categoryName = "Salary";
        when(categoryRepository.existsByCategory("SALARY")).thenReturn(false);

        // Act
        categoriesService.addNewCategory(categoryName, 100);

        // Assert
        var captor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(captor.capture());

        assertEquals("SALARY", captor.getValue().getCategory());
        assertEquals("Income", captor.getValue().getType());
    }

    @Test
    void addNewCategory_NegativeValue_ShouldSaveAsExpense() {
        // Arrange
        var categoryName = "Groceries";
        when(categoryRepository.existsByCategory("GROCERIES")).thenReturn(false);

        // Act
        categoriesService.addNewCategory(categoryName, -50);

        // Assert
        var captor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(captor.capture());

        assertEquals("GROCERIES", captor.getValue().getCategory());
        assertEquals("Expense", captor.getValue().getType());
    }

    @Test
    void addNewCategory_ZeroValue_ShouldSaveAsNullType() {
        // Arrange
        var categoryName = "Adjustment";
        when(categoryRepository.existsByCategory("ADJUSTMENT")).thenReturn(false);

        // Act
        categoriesService.addNewCategory(categoryName, 0);

        // Assert
        var captor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(captor.capture());

        assertEquals("ADJUSTMENT", captor.getValue().getCategory());
        assertEquals("Null", captor.getValue().getType());
    }

    @Test
    void addNewCategory_CategoryAlreadyExists_ShouldNotSave() {
        // Arrange
        var categoryName = "Food";
        when(categoryRepository.existsByCategory("FOOD")).thenReturn(true);

        // Act
        categoriesService.addNewCategory(categoryName, 50);

        // Assert
        verify(categoryRepository, never()).save(any(CategoryEntity.class));
    }

    // --- Tests for getCategory ---

    @Test
    void getCategory_CategoryExists_ShouldReturnEntity() {
        // Arrange
        var categoryName = "Housing";
        var mockEntity = new CategoryEntity();
        mockEntity.setCategory("HOUSING");

        when(categoryRepository.getCategoryEntitiesByCategory("HOUSING")).thenReturn(mockEntity);

        // Act
        var result = categoriesService.getCategory(categoryName);

        // Assert
        assertNotNull(result);
        assertEquals("HOUSING", result.getCategory());
        verify(categoryRepository, never()).save(any()); // Proves we didn't try to create it
    }

    @Test
    void getCategory_CategoryDoesNotExist_ShouldCreateAndReturnEntity() {
        // Arrange
        var categoryName = "Utilities";
        var newMockEntity = new CategoryEntity();
        newMockEntity.setCategory("UTILITIES");

        when(categoryRepository.getCategoryEntitiesByCategory("UTILITIES"))
                .thenReturn(null)
                .thenReturn(newMockEntity);

        when(categoryRepository.existsByCategory("UTILITIES")).thenReturn(false);

        // Act
        var result = categoriesService.getCategory(categoryName);

        // Assert
        assertNotNull(result);
        assertEquals("UTILITIES", result.getCategory());

        var captor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(captor.capture());
        assertEquals("Null", captor.getValue().getType());

        verify(categoryRepository, times(2)).getCategoryEntitiesByCategory("UTILITIES");
    }

    // --- Tests for getCategoryId ---

    @Test
    void getCategoryId_CategoryExists_ShouldReturnId() {
        // Arrange
        var categoryName = "Bills";
        when(categoryRepository.getCategoryId("BILLS")).thenReturn(Optional.of(5L));

        // Act
        var id = categoriesService.getCategoryId(categoryName);

        // Assert
        assertEquals(5L, id);
    }

    @Test
    void getCategoryId_CategoryDoesNotExist_ShouldThrowException() {
        // Arrange
        var categoryName = "Unknown";
        when(categoryRepository.getCategoryId("UNKNOWN")).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                categoriesService.getCategoryId(categoryName)
        );
        assertEquals("Category not found", exception.getMessage());
    }
}