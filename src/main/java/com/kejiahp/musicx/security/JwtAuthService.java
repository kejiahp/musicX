package com.kejiahp.musicx.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import com.kejiahp.musicx.apps.user.UserModel;

@Component
public class JwtAuthService {
    @Value("${app.jwt.secret_key}")
    private String JWT_SECRET;

    private SecretKey generateSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.JWT_SECRET);
        // byte[] keyBytes = this.JWT_SECRET.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 256 bits (32 bytes)");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private final int EXPIRATION_DAYS = 1;

    public String generateToken(final UserModel user, String sid) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("sid", sid)
                .issuedAt(new Date())
                .expiration(Date.from(now.plus(Duration.ofDays(EXPIRATION_DAYS))))
                .signWith(generateSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Optional<String> extractSid(String token) {
        try {
            return Optional.of(
                    Jwts.parser()
                            .verifyWith(generateSecretKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload()
                            .get("sid",
                                    String.class));
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }

    }

    public Optional<String> extractId(String token) {
        try {
            return Optional.of(
                    Jwts.parser()
                            .verifyWith(generateSecretKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload()
                            .getSubject());
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public boolean isTokenIdValid(String token, UserModel user) {
        String userId = extractId(token).orElse("");

        if (userId.isEmpty())
            return false;

        return userId.equals(user.getId().toString());
    }
}
