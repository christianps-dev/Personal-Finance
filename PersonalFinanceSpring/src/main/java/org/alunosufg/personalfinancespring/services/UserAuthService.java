package org.alunosufg.personalfinancespring.services;

import org.alunosufg.personalfinancespring.dto.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    UserAuthRepository userAuthRepository;
    PasswordEncoder passwordEncoder;

    public UserAuthService(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existingUserInDatabase(RegisterRequestDTO newUser){
        return userAuthRepository.existsUserEntityByEmail(newUser.email()) ||
                userAuthRepository.existsUserEntityByUsername(newUser.password());
    }

}
