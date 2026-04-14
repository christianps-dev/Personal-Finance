package org.alunosufg.personalfinancespring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.UserAuthRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserAuthRepository userAuthRepository;

    public SecurityFilter(TokenService tokenService, UserAuthRepository userAuthRepository) {
        this.tokenService = tokenService;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);
        var login = tokenService.validateToken(token);

        if (login != null) {
            UserEntity user = userAuthRepository.findByEmail(login).orElse(null);
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            assert user != null;
            var authentication = new UsernamePasswordAuthenticationToken(user, login, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if (token == null) return null;
        return token.replace("Bearer ", "");
    }
}
