package org.alunosufg.personalfinancespring.budgets;

import org.alunosufg.personalfinancespring.dto.budgets.BudgetDTO;
import org.alunosufg.personalfinancespring.dto.budgets.BudgetLimitDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.entities.BudgetEntity;
import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.alunosufg.personalfinancespring.repository.BudgetRepository;
import org.alunosufg.personalfinancespring.services.AccountsServices;
import org.alunosufg.personalfinancespring.services.BudgetServices;
import org.alunosufg.personalfinancespring.services.CategoriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetsTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoriesService categoriesService;

    @Mock
    private AccountsServices accountsServices;

    @InjectMocks
    private BudgetServices budgetServices;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_CATEGORY = "FOOD";
    private static final Integer TEST_MONTH = 8;
    private static final Long TEST_CATEGORY_ID = 10L;

    // --- Tests for addNewBudget ---

    @Test
    void addNewBudget_BudgetDoesNotExist_ShouldSaveNewBudget() {
        // Arrange
        var mockDto = mock(BudgetDTO.class);
        var mockAccount = new AccountEntity();
        var mockCategory = new CategoryEntity();

        when(mockDto.getCategory()).thenReturn(TEST_CATEGORY);
        when(mockDto.getMonth()).thenReturn(TEST_MONTH);
        when(mockDto.getBudgetLimit()).thenReturn(5000);

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);
        when(categoriesService.getCategoryId(TEST_CATEGORY)).thenReturn(TEST_CATEGORY_ID);

        when(budgetRepository.existsBudget(mockAccount, TEST_CATEGORY_ID, TEST_MONTH))
                .thenReturn(Optional.empty());

        when(categoriesService.getCategory(TEST_CATEGORY)).thenReturn(mockCategory);

        // Act
        var result = budgetServices.addNewBudget(mockDto, TEST_EMAIL);

        // Assert
        assertEquals(mockDto, result);

        var captor = ArgumentCaptor.forClass(BudgetEntity.class);
        verify(budgetRepository).save(captor.capture());
        verify(budgetRepository, never()).updateBudget(any(), any(), any(), any());

        var savedBudget = captor.getValue();
        assertEquals(mockAccount, savedBudget.getAccount());
        assertEquals(mockCategory, savedBudget.getCategory());
        assertEquals(TEST_MONTH, savedBudget.getMonth());
        assertEquals(5000, savedBudget.getBudgetLimit());
    }

    @Test
    void addNewBudget_BudgetExists_ShouldUpdateExistingBudget() {
        // Arrange
        var mockDto = mock(BudgetDTO.class);
        var mockAccount = new AccountEntity();
        var existingBudget = new BudgetEntity();

        when(mockDto.getCategory()).thenReturn(TEST_CATEGORY);
        when(mockDto.getMonth()).thenReturn(TEST_MONTH);
        when(mockDto.getBudgetLimit()).thenReturn(7500);

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);
        when(categoriesService.getCategoryId(TEST_CATEGORY)).thenReturn(TEST_CATEGORY_ID);

        when(budgetRepository.existsBudget(mockAccount, TEST_CATEGORY_ID, TEST_MONTH))
                .thenReturn(Optional.of(existingBudget));

        // Act
        var result = budgetServices.addNewBudget(mockDto, TEST_EMAIL);

        // Assert
        assertEquals(mockDto, result);

        verify(budgetRepository).updateBudget(mockAccount, TEST_CATEGORY_ID, TEST_MONTH, 7500);

        verify(budgetRepository, never()).save(any(BudgetEntity.class));
    }

    // --- Tests for existBudget ---

    @Test
    void existBudget_WhenFound_ShouldReturnTrue() {
        // Arrange
        var mockDto = mock(BudgetDTO.class);
        var mockAccount = new AccountEntity();

        when(mockDto.getCategory()).thenReturn(TEST_CATEGORY);
        when(mockDto.getMonth()).thenReturn(TEST_MONTH);

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);
        when(categoriesService.getCategoryId(TEST_CATEGORY)).thenReturn(TEST_CATEGORY_ID);
        when(budgetRepository.existsBudget(mockAccount, TEST_CATEGORY_ID, TEST_MONTH))
                .thenReturn(Optional.of(new BudgetEntity()));

        // Act
        var result = budgetServices.existBudget(mockDto, TEST_EMAIL);

        // Assert
        assertTrue(result);
    }

    @Test
    void existBudget_WhenNotFound_ShouldReturnFalse() {
        // Arrange
        var mockDto = mock(BudgetDTO.class);
        var mockAccount = new AccountEntity();

        when(mockDto.getCategory()).thenReturn(TEST_CATEGORY);
        when(mockDto.getMonth()).thenReturn(TEST_MONTH);

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);
        when(categoriesService.getCategoryId(TEST_CATEGORY)).thenReturn(TEST_CATEGORY_ID);
        when(budgetRepository.existsBudget(mockAccount, TEST_CATEGORY_ID, TEST_MONTH))
                .thenReturn(Optional.empty());

        // Act
        var result = budgetServices.existBudget(mockDto, TEST_EMAIL);

        // Assert
        assertFalse(result);
    }

    // --- Tests for getBudgets ---

    @Test
    void getBudgets_ShouldReturnBudgetList() {
        // Arrange
        var mockAccount = new AccountEntity();
        var expectedList = List.of(mock(BudgetDTO.class));

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);
        when(budgetRepository.getBudgets(mockAccount, TEST_MONTH)).thenReturn(expectedList);

        // Act
        var result = budgetServices.getBudgets(TEST_EMAIL, TEST_MONTH);

        // Assert
        assertEquals(expectedList, result);
        assertEquals(1, result.size());
    }

    // --- Tests for getBudgetsLimits ---

    @Test
    void getBudgetsLimits_ShouldFormatDatesCorrectlyAndReturnList() {
        // Arrange
        var mockAccount = new AccountEntity();
        var expectedList = List.of(mock(BudgetLimitDTO.class));

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);

        when(budgetRepository.getBudgetsLimitsAndSpends(eq(mockAccount), any(Date.class), any(Date.class)))
                .thenReturn(expectedList);

        // Act
        var result = budgetServices.getBudgetsLimits(TEST_EMAIL, 5);

        // Assert
        assertEquals(expectedList, result);

        var firstDayCaptor = ArgumentCaptor.forClass(Date.class);
        var lastDayCaptor = ArgumentCaptor.forClass(Date.class);

        verify(budgetRepository).getBudgetsLimitsAndSpends(
                eq(mockAccount),
                firstDayCaptor.capture(),
                lastDayCaptor.capture()
        );

        assertEquals("2026-05-01", firstDayCaptor.getValue().toString());
        assertEquals("2026-05-31", lastDayCaptor.getValue().toString());
    }
}