package com.truchi.vastragruh.service;

import com.truchi.vastragruh.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public String generateToken(String username) {

        Date now = new Date();

        Date expiry =
                new Date(now.getTime() + properties.getExpiration());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey())
                .compact();

    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();

    }

    public boolean isTokenValid(String token,
                                String username) {

        return username.equals(extractUsername(token))
                && !isExpired(token);

    }

    private boolean isExpired(String token) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());

    }

    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private SecretKey getKey() {

        byte[] key =
                Decoders.BASE64.decode(properties.getSecret());

        return Keys.hmacShaKeyFor(
                properties.getSecret().getBytes(StandardCharsets.UTF_8));

    }

}