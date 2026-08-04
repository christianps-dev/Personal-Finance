package org.alunosufg.personalfinancespring.transactions;

import org.alunosufg.personalfinancespring.dto.transactions.TransactionDTO;
import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.alunosufg.personalfinancespring.entities.TransactionEntity;
import org.alunosufg.personalfinancespring.repository.TransactionRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.alunosufg.personalfinancespring.services.AccountsServices;
import org.alunosufg.personalfinancespring.services.CategoriesService;
import org.alunosufg.personalfinancespring.services.TransactionServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserAuthRepository userAuthRepository;

    @Mock
    private CategoriesService categoriesService;

    @Mock
    private AccountsServices accountsServices;

    @InjectMocks
    private TransactionServices transactionServices;

    private static final String TEST_EMAIL = "test@example.com";

    // --- Tests for saveTransaction ---

    @Test
    void saveTransaction_WithProvidedDate_ShouldSaveSuccessfully() {
        // Arrange
        var testDate = new Date(Instant.now().getEpochSecond());
        var dto = new UserTransactionDTO(
                15000, "Food", "Groceries", testDate
        );
        var mockAccount = new AccountEntity();
        var mockCategory = new CategoryEntity();

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(mockAccount);
        when(categoriesService.getCategory(dto.category())).thenReturn(mockCategory);

        var result = transactionServices.saveTransaction(dto, TEST_EMAIL);

        assertTrue(result);
        verify(accountsServices, times(1)).updateBalance(TEST_EMAIL, dto.value());

        var captor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(transactionRepository, times(1)).save(captor.capture());

        var savedEntity = captor.getValue();
        assertEquals(dto.value(), savedEntity.getValue());
        assertEquals(mockAccount, savedEntity.getAccount());
        assertEquals(mockCategory, savedEntity.getCategory());
        assertEquals(testDate, savedEntity.getTransactionTime());
    }

    @Test
    void saveTransaction_WithoutProvidedDate_ShouldUseCurrentDate() {
        // Arrange
        var dto = new UserTransactionDTO(
                5000, "Food", "Snacks", null
        );

        when(accountsServices.getAccount(TEST_EMAIL)).thenReturn(new AccountEntity());
        when(categoriesService.getCategory(dto.category())).thenReturn(new CategoryEntity());

        // Act
        transactionServices.saveTransaction(dto, TEST_EMAIL);

        // Assert
        var captor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(transactionRepository).save(captor.capture());

        assertNotNull(captor.getValue().getTransactionTime());
    }

    // --- Tests for getAllTransactions ---

    @Test
    void getAllTransactions_UserExists_ShouldReturnTransactionList() {
        // Arrange
        var userId = 1L;
        when(userAuthRepository.findIdByEmail(TEST_EMAIL)).thenReturn(userId);

        var expectedTransactions = List.of(mock(TransactionDTO.class));
        when(transactionRepository.getAllByUserId(userId)).thenReturn(expectedTransactions);

        // Act
        var result = transactionServices.getAllTransactions(TEST_EMAIL);

        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedTransactions, result);
    }

    @Test
    void getAllTransactions_UserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userAuthRepository.findIdByEmail(TEST_EMAIL)).thenReturn(null);

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                transactionServices.getAllTransactions(TEST_EMAIL)
        );

        assertEquals("User not found", exception.getMessage());
        verify(transactionRepository, never()).getAllByUserId(any());
    }

    // --- Tests for getALlTransactionsByMonth ---

    @Test
    void getAllTransactionsByMonth_ShouldFormatDatesCorrectly() {
        // Arrange
        var userId = 1L;
        var month = 5;
        when(userAuthRepository.findIdByEmail(TEST_EMAIL)).thenReturn(userId);

        // Act
        transactionServices.getALlTransactionsByMonth(TEST_EMAIL, month);

        // Assert
        var firstDayCaptor = ArgumentCaptor.forClass(Date.class);
        var lastDayCaptor = ArgumentCaptor.forClass(Date.class);

        verify(transactionRepository).getAllTransactionsByMonth(
                firstDayCaptor.capture(),
                lastDayCaptor.capture(),
                eq(userId)
        );

        assertEquals("2026-05-01", firstDayCaptor.getValue().toString());
        assertEquals("2026-05-31", lastDayCaptor.getValue().toString());
    }
}