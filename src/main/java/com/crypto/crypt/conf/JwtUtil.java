package com.crypto.crypt.conf;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    // Clé secrète définie manuellement
    private static final String SECRET_KEY_STRING = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000; // 10 heures

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    // Générer un token
    public String generateToken(String subject, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, subject);
    }

    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, subject);
    }

    // Extraire le sujet (subject) du token
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire une réclamation (claim) personnalisée
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractCustomClaim(String token, String claimKey) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimKey, String.class); // Extrait la valeur du claim spécifié par la clé
    }

    // Vérifier si le token est expiré
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraire la date d'expiration du token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Utilisation de la clé secrète définie manuellement
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Créer le token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY) // Utilisation de la clé secrète définie manuellement
                .compact();
    }

    public Timestamp getInstantExpiration() {
        return new Timestamp(System.currentTimeMillis() + EXPIRATION_TIME);
    }
}
