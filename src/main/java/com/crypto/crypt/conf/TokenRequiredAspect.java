package com.crypto.crypt.conf;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.entityframework.http.TokenRequired;
import org.springframework.stereotype.Component;

import com.crypto.crypt.err.TokenValidationException;
import com.crypto.crypt.service.TokenService;

import java.lang.reflect.Method;

@Component
@Aspect
public class TokenRequiredAspect {
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    public TokenRequiredAspect(HttpServletRequest request, JwtUtil jwtUtil) {
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    @Before("@annotation(org.entityframework.http.TokenRequired)")
    public void checkToken(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TokenRequired tokenRequired = method.getAnnotation(TokenRequired.class);
        String authorizedRole = tokenRequired.authorizedRole(); // Rôle attendu

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new TokenValidationException("Token manquant ou invalide");
        }

        String token = authorizationHeader.substring(7);

        if (!validateToken(token, authorizedRole)) {
            throw new TokenValidationException("Accès refusé : token invalide ou expiré");
        }
    }

    private boolean validateToken(String token, String authorizedRole) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new TokenValidationException("Token expiré");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!authorizedRole.isEmpty()) {
            String userRole = jwtUtil.extractCustomClaim(token, "role");

            if (userRole == null || !userRole.equals(authorizedRole)) {
                throw new TokenValidationException("Rôle non autorisé");
            }
        }

        try (TokenService tokenService = new TokenService()) {
            return tokenService.isValid(token);
        } catch (Exception e) {
            throw new Error("Application Error");
        }
    }
}
