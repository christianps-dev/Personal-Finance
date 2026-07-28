package org.alunosufg.personalfinancespring.controller;

import jakarta.validation.Valid;
import org.alunosufg.personalfinancespring.dto.auth.ChangePasswordDTO;
import org.alunosufg.personalfinancespring.dto.auth.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.auth.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.dto.auth.ResponseDTO;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.security.TokenService;
import org.alunosufg.personalfinancespring.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "${frontend.angular.url}")
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final TokenService tokenService;

    public UserAuthController(UserAuthService userAuthService, TokenService tokenService) {
        this.userAuthService = userAuthService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterRequestDTO body) {

        UserEntity newUser = userAuthService.registerUser(body);
        return ResponseEntity.ok(new ResponseDTO(newUser.getUsername(), newUser.getEmail(), tokenService.generateToken(newUser.getEmail())));

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginAuthDTO body) {

        ResponseDTO response = userAuthService.loginUser(body);
        return ResponseEntity.ok(new ResponseDTO(response.username(), response.email(), tokenService.generateToken(response.email())));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO body) {

        userAuthService.changePassword(body);
        return ResponseEntity.noContent().build();
    }
}