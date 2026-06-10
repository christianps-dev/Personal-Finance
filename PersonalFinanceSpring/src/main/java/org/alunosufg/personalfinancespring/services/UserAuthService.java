package org.alunosufg.personalfinancespring.services;

import jakarta.transaction.Transactional;
import org.alunosufg.personalfinancespring.dto.auth.ChangePasswordDTO;
import org.alunosufg.personalfinancespring.dto.auth.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.auth.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.dto.auth.ResponseDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.alunosufg.personalfinancespring.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Objects;

@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;
    AccountRepository accountRepository;

    public UserAuthService(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder,
                           TokenService tokenService, AccountRepository accountRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public UserEntity registerUser(RegisterRequestDTO userReg) {

        if (userAuthRepository.existsUserEntityByEmail(userReg.email())) {
            throw new RuntimeException("Email already in use");
        }

        if (userAuthRepository.existsUserEntityByUsername(userReg.username())) {
            throw new RuntimeException("Username already taken");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(Objects.requireNonNull(passwordEncoder.encode(userReg.password())));
        userEntity.setUsername(userReg.username());
        userEntity.setEmail(userReg.email());
        userEntity.setCreated(Date.from(Instant.now()));

        UserEntity savedUser = userAuthRepository.save(userEntity);
        createUserAccount(savedUser);

        return savedUser;
    }

    public ResponseDTO loginUser(LoginAuthDTO userLog) {
        UserEntity user = userAuthRepository.findByEmail(userLog.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(userLog.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new ResponseDTO(user.getUsername(), user.getEmail(), tokenService.generateToken(user.getEmail()));
    }

    private void createUserAccount(UserEntity user) {

        AccountEntity account = new AccountEntity();
        account.setAccountBalance(0);
        account.setUser(user);
        accountRepository.save(account);
    }

    @Transactional
    public void changePassword(ChangePasswordDTO body) {
        UserEntity user = getUser(body.email());

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new RuntimeException("Current password incorrect");
        }

        if (body.password().equals(body.newPassword())) {
            throw new RuntimeException("New password cannot be the same as old password");
        }

        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(body.newPassword())));
        userAuthRepository.save(user);
    }

    public UserEntity getUser(String email){
        return userAuthRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public Long getUserId(String email){
        return userAuthRepository.getIdByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }



}
