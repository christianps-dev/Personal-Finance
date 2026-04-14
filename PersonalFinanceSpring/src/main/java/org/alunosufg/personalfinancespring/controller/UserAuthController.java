package org.alunosufg.personalfinancespring.controller;

import jakarta.validation.Valid;
import org.alunosufg.personalfinancespring.dto.auth.LoginAuthDTO;
import org.alunosufg.personalfinancespring.dto.auth.RegisterRequestDTO;
import org.alunosufg.personalfinancespring.dto.auth.ResponseDTO;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService ){
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterRequestDTO body)  {

        if ( body == null)
            return userAuthService.wrongAuthCredentials("null");

        if (!userAuthService.existingUserInDatabase(body))
            return userAuthService.credentialsAlreadyUsed(body);

        UserEntity newUser = userAuthService.registerUser(body);
        return userAuthService.authUserResponse(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginAuthDTO body) {

        if ( body == null)
            return userAuthService.wrongAuthCredentials("null");

        UserEntity userLog = userAuthService.loginUser(body);

        if (userLog == null)
            return userAuthService.wrongAuthCredentials("Not found");

        return userAuthService.authUserResponse(userLog);
    }

}
