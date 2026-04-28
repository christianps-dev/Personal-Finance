package org.alunosufg.personalfinancespring.services;

import org.alunosufg.personalfinancespring.dto.auth.ChangePasswordDTO;
import org.alunosufg.personalfinancespring.dto.auth.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.auth.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.dto.auth.ResponseDTO;
import org.alunosufg.personalfinancespring.entities.AccountEntity;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.AccountRepository;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.alunosufg.personalfinancespring.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Objects;

@Service
public class UserAuthService {

    UserAuthRepository userAuthRepository;
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

    public boolean existingUserInDatabase(RegisterRequestDTO newUser){
        return userAuthRepository.existsUserEntityByEmail(newUser.email()) ||
                userAuthRepository.existsUserEntityByUsername(newUser.password());
    }

    public UserEntity registerUser(RegisterRequestDTO userReg){

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(Objects.requireNonNull(passwordEncoder.encode(userReg.password())));
        userEntity.setUsername(userReg.username());
        userEntity.setEmail(userReg.email());

        userEntity.setCreated(Date.from(Instant.now()));
        userAuthRepository.save(userEntity);

        createUserAccount(userEntity);

        return userEntity;
    }

    public UserEntity loginUser(LoginAuthDTO userLog) {

        UserEntity loggedUser = userAuthRepository.findByEmail(userLog.email()).orElse(null);

        if (loggedUser != null && passwordEncoder.matches(userLog.password(), loggedUser.getPassword()))
            return loggedUser;

        return null;
    }

    public void createUserAccount(UserEntity user){
        AccountEntity account = new AccountEntity();
        account.setAccountBalance(0);
        account.setUser(user);

        accountRepository.save(account);
    }

    public ResponseEntity<ResponseDTO> authUserResponse(UserEntity userLog){
        return ResponseEntity.ok(new ResponseDTO(userLog.getUsername(),userLog.getEmail(), tokenService.generateToken(userLog)));
    }

    public ResponseEntity<ResponseDTO> wrongAuthCredentials(String error){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(error, error, error));
    }

    public ResponseEntity<ResponseDTO> credentialsAlreadyUsed(RegisterRequestDTO userReg){
        String responseStr = "";
        if (userAuthRepository.existsUserEntityByUsername(userReg.username()))
            responseStr += "username used";

        if (userAuthRepository.existsUserEntityByEmail(userReg.email()))
            responseStr += ", email used";

        return wrongAuthCredentials(responseStr);

    }

    public boolean changePassword(ChangePasswordDTO body){
        UserEntity user = userAuthRepository.findByEmail(body.email()).orElse(null);
        if (user == null || body.password().equals(body.newPassword()))
            return false;

        if (passwordEncoder.matches(body.password(), user.getPassword())){
            userAuthRepository.changeUserPassword(user.getId(), passwordEncoder.encode(body.newPassword()));
            System.out.println("Password changed successfully");
            return true;
        }
        else{
            System.out.println("Password change failed");

            return false;
        }
    }

}
