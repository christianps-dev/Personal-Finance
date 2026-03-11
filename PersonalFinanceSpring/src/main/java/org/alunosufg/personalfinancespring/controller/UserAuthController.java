package org.alunosufg.personalfinancespring.controller;

import org.alunosufg.personalfinancespring.dto.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.dto.ResponseDTO;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.alunosufg.personalfinancespring.security.TokenService;
import org.alunosufg.personalfinancespring.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController()
@RequestMapping("/auth")
@CrossOrigin(origins = "${ANGULAR_FRONTEND_URL}")
public class UserAuthController {
    private final TokenService tokenService;
    private final UserAuthRepository userAuthRepository;
    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;

    public UserAuthController(TokenService tokenService, UserAuthRepository userAuthRepository,
                              UserAuthService userAuthService, PasswordEncoder passwordEncoder){
        this.tokenService = tokenService;
        this.userAuthRepository = userAuthRepository;
        this.userAuthService = userAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        if ( body!= null && !userAuthService.existingUserInDatabase(body)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(Objects.requireNonNull(passwordEncoder.encode(body.password())));
            userEntity.setUsername(body.username());
            userEntity.setEmail(body.email());
            userAuthRepository.save(userEntity);
            String token = tokenService.generateToken(userEntity);
            return ResponseEntity.ok(new ResponseDTO(userEntity.getEmail(), token));
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginAuthDTO body) {
        UserEntity user = userAuthRepository.findByEmail(body.email()).orElse(null);
        assert user != null;
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getEmail(), token));
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

}
