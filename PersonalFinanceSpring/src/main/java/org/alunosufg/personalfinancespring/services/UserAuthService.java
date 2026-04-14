package org.alunosufg.personalfinancespring.services;

import org.alunosufg.personalfinancespring.dto.auth.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.auth.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.dto.auth.ResponseDTO;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.alunosufg.personalfinancespring.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class UserAuthService {

    UserAuthRepository userAuthRepository;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;

    public UserAuthService(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
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
        userEntity.setCreated(getDay());
        userAuthRepository.save(userEntity);

        return userEntity;
    }

    public UserEntity loginUser(LoginAuthDTO userLog) {

        UserEntity loggedUser = userAuthRepository.findByEmail(userLog.email()).orElse(null);

        if (loggedUser != null && passwordEncoder.matches(userLog.password(), loggedUser.getPassword()))
            return loggedUser;

        return null;
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

    public String getDay(){
        LocalDateTime localTime = LocalDateTime.now();
        return localTime.format(DateTimeFormatter.ISO_DATE);
    }

}
