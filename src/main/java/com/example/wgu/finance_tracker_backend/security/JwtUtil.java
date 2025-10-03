package com.example.wgu.finance_tracker_backend.security;

import com.example.wgu.finance_tracker_backend.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    public String createJwtToken(User user) {

        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        return token;
    }
}
