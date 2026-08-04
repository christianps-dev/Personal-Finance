package org.alunosufg.personalfinancespring.accounts;

import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.alunosufg.personalfinancespring.services.AccountsServices;
import org.alunosufg.personalfinancespring.services.UserAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private AccountsServices accountsServices;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    // --- Tests for getAccount ---

    @Test
    void getAccount_AccountExists_ShouldReturnAccount() {
        // Arrange
        var mockAccount = new AccountEntity();

        when(userAuthService.getUserId(TEST_EMAIL)).thenReturn(TEST_USER_ID);
        when(accountRepository.getAccount(TEST_USER_ID)).thenReturn(Optional.of(mockAccount));

        // Act
        var result = accountsServices.getAccount(TEST_EMAIL);

        // Assert
        assertNotNull(result);
        assertEquals(mockAccount, result);
        verify(userAuthService, times(1)).getUserId(TEST_EMAIL);
        verify(accountRepository, times(1)).getAccount(TEST_USER_ID);
    }

    @Test
    void getAccount_AccountDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userAuthService.getUserId(TEST_EMAIL)).thenReturn(TEST_USER_ID);
        when(accountRepository.getAccount(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                accountsServices.getAccount(TEST_EMAIL)
        );
        assertEquals("Account not found", exception.getMessage());
    }

    // --- Tests for updateBalance ---

    @Test
    void updateBalance_PositiveValue_ShouldIncreaseBalance() {
        // Arrange
        var initialBalance = 100;
        var valueToAdd = 50;

        var mockAccount = new AccountEntity();
        mockAccount.setAccountBalance(initialBalance);

        when(userAuthService.getUserId(TEST_EMAIL)).thenReturn(TEST_USER_ID);
        when(accountRepository.getAccount(TEST_USER_ID)).thenReturn(Optional.of(mockAccount));

        // Act
        accountsServices.updateBalance(TEST_EMAIL, valueToAdd);

        // Assert
        var captor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(accountRepository).save(captor.capture());

        assertEquals(150, captor.getValue().getAccountBalance());
    }

    @Test
    void updateBalance_NegativeValue_ShouldDecreaseBalance() {
        // Arrange
        var initialBalance = 200;
        var valueToSubtract = -75;

        var mockAccount = new AccountEntity();
        mockAccount.setAccountBalance(initialBalance);

        when(userAuthService.getUserId(TEST_EMAIL)).thenReturn(TEST_USER_ID);
        when(accountRepository.getAccount(TEST_USER_ID)).thenReturn(Optional.of(mockAccount));

        // Act
        accountsServices.updateBalance(TEST_EMAIL, valueToSubtract);

        // Assert
        var captor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(accountRepository).save(captor.capture());

        assertEquals(125, captor.getValue().getAccountBalance());
    }
}