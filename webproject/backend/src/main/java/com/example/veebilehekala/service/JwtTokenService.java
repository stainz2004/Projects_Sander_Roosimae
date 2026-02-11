package com.example.veebilehekala.service;

import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Role;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final SecretKey key;

    public String generateToken(Customer customer) {
        String roleName = customer.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("ROLE_USER");

        return Jwts.builder()
                .subject(customer.getUsername())
                .claims(Map.of(
                        "userId", customer.getId(),
                        "name", customer.getName(),
                        "role", roleName
                ))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 100 * 600 * 60 * 24))
                .signWith(key)
                .compact();
    }
}