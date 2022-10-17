package com.rpi.alexandria.service.security;

import com.rpi.alexandria.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
@Slf4j
public class JwtService {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private final SignatureAlgorithm signatureAlgorithm = HS256;
    private final String key;


    public JwtService(@Value("${application.security.jwt.key}") String key) {
        this.key = key;
    }

    public String getJwt(final User user) {
        final Instant now = Instant.now();
        Key secretKey = new SecretKeySpec(key.getBytes(),
                signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .claim(NAME, String.format("%s %s", user.getFirstName(), user.getLastName()))
                .claim(EMAIL, user.getPrimaryEmail())
                .setSubject(user.getUsername())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .signWith(secretKey)
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.DAYS)))
                .compact();
    }

    private Jws<Claims> getClaims(String jwt){
        Key secretKey = new SecretKeySpec(key.getBytes(),
                signatureAlgorithm.getJcaName());
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt);
    }

    public String getUsername(String jwt){
        Jws<Claims> claims = getClaims(jwt);
        return claims.getBody().getSubject();
    }

    public boolean validate(final String tokenString) {
        try {
            getClaims(tokenString);
            return true;
        } catch (Exception e) {
            log.error("Failed to parse JWT: {}", e.getMessage());
        }
        return false;
    }
}
