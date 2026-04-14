package org.alunosufg.personalfinancespring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class TokenService {

    @Value("${token.secret.key}")
    private String secret;
    
    public String generateToken(UserEntity newUser) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("user-auth-service")
                    .withSubject(newUser.getEmail())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public Instant getExpirationDate() {
        return Instant.now().plusSeconds(3600);
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            System.out.println("--- Validating token ---");
            return JWT.require(algorithm).
                    withIssuer("user-auth-service")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            System.out.println("--- ERROR: Token invalid ---");

            return null;
        }
    }
}
