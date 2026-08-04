package org.alunosufg.personalfinancespring.authentication;

import org.alunosufg.personalfinancespring.dto.auth.ChangePasswordDTO;
import org.alunosufg.personalfinancespring.dto.auth.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.auth.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.alunosufg.personalfinancespring.security.TokenService;
import org.alunosufg.personalfinancespring.services.UserAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthTests {

    @Mock
    private UserAuthRepository userAuthRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USER = "testuser";
    private static final String RAW_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword123";

    // --- Tests for registerUser ---

    @Test
    void registerUser_ValidData_ShouldCreateUserAndAccount() {
        // Arrange
        var request = new RegisterRequestDTO(TEST_EMAIL, RAW_PASSWORD, TEST_USER);

        when(userAuthRepository.existsUserEntityByEmail(TEST_EMAIL)).thenReturn(false);
        when(userAuthRepository.existsUserEntityByUsername(TEST_USER)).thenReturn(false);
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // Mock the save to return the user (simulating database behavior)
        var savedMockUser = new UserEntity();
        savedMockUser.setEmail(TEST_EMAIL);
        when(userAuthRepository.save(any(UserEntity.class))).thenReturn(savedMockUser);

        // Act
        var result = userAuthService.registerUser(request);

        // Assert
        assertNotNull(result);

        // Verify User was saved properly
        var userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userAuthRepository).save(userCaptor.capture());
        assertEquals(TEST_EMAIL, userCaptor.getValue().getEmail());
        assertEquals(TEST_USER, userCaptor.getValue().getUsername());
        assertEquals(ENCODED_PASSWORD, userCaptor.getValue().getPassword());
        assertNotNull(userCaptor.getValue().getCreated());

        // Verify Account was created implicitly
        var accountCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertEquals(0, accountCaptor.getValue().getAccountBalance());
        assertEquals(savedMockUser, accountCaptor.getValue().getUser());
    }

    @Test
    void registerUser_EmailExists_ShouldThrowException() {
        // Arrange
        var request = new RegisterRequestDTO(TEST_EMAIL, RAW_PASSWORD, TEST_USER);
        when(userAuthRepository.existsUserEntityByEmail(TEST_EMAIL)).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                userAuthService.registerUser(request)
        );
        assertEquals("Email already in use", exception.getMessage());
        verify(userAuthRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void registerUser_UsernameExists_ShouldThrowException() {
        // Arrange
        var request = new RegisterRequestDTO(TEST_EMAIL, RAW_PASSWORD, TEST_USER);
        when(userAuthRepository.existsUserEntityByUsername(TEST_USER)).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                userAuthService.registerUser(request)
        );
        assertEquals("Username already taken", exception.getMessage());
        verify(userAuthRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
    }

    // --- Tests for loginUser ---

    @Test
    void loginUser_ValidCredentials_ShouldReturnResponseDTOWithToken() {
        // Arrange
        var loginRequest = new LoginAuthDTO(TEST_EMAIL, RAW_PASSWORD);
        var mockUser = new UserEntity();
        mockUser.setEmail(TEST_EMAIL);
        mockUser.setUsername(TEST_USER);
        mockUser.setPassword(ENCODED_PASSWORD);

        when(userAuthRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(tokenService.generateToken(TEST_EMAIL)).thenReturn("mocked-jwt-token");

        // Act
        var response = userAuthService.loginUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_USER, response.username());
        assertEquals(TEST_EMAIL, response.email());
        assertEquals("mocked-jwt-token", response.token());
    }

    @Test
    void loginUser_InvalidPassword_ShouldThrowException() {
        // Arrange
        var loginRequest = new LoginAuthDTO(TEST_EMAIL, "wrongPassword");
        var mockUser = new UserEntity();
        mockUser.setPassword(ENCODED_PASSWORD);

        when(userAuthRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPassword", ENCODED_PASSWORD)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                userAuthService.loginUser(loginRequest)
        );
        assertEquals("Invalid email or password", exception.getMessage());
    }

    // --- Tests for changePassword ---

    @Test
    void changePassword_ValidRequest_ShouldUpdatePassword() {
        // Arrange
        var newPassword = "newPassword456";
        var encodedNewPassword = "encodedNewPassword456";
        var request = new ChangePasswordDTO(TEST_EMAIL, RAW_PASSWORD, newPassword);

        var mockUser = new UserEntity();
        mockUser.setPassword(ENCODED_PASSWORD);

        when(userAuthRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        // Act
        userAuthService.changePassword(request);

        // Assert
        var userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userAuthRepository).save(userCaptor.capture());
        assertEquals(encodedNewPassword, userCaptor.getValue().getPassword());
    }

    @Test
    void changePassword_SameAsOldPassword_ShouldThrowException() {
        // Arrange
        var request = new ChangePasswordDTO(TEST_EMAIL, RAW_PASSWORD, RAW_PASSWORD); // Same password
        var mockUser = new UserEntity();
        mockUser.setPassword(ENCODED_PASSWORD);

        when(userAuthRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () ->
                userAuthService.changePassword(request)
        );
        assertEquals("New password cannot be the same as old password", exception.getMessage());
        verify(userAuthRepository, never()).save(any());
    }
}