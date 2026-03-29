package com.example.veebilehekala.service;

import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Role;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = Jwts.SIG.HS256.key().build();
        jwtTokenService = new JwtTokenService(secretKey);
    }

    @Test
    void generateToken_ShouldReturnValidJwtToken() {
        Role role = Role.builder().name("ROLE_ADMIN").build();
        Customer customer = Customer.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .roles(Set.of(role))
                .build();

        String token = jwtTokenService.generateToken(customer);

        assertNotNull(token);

        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("testuser", claims.getSubject());
        assertEquals(1L, claims.get("userId", Long.class));
        assertEquals("Test User", claims.get("name"));
        assertEquals("ROLE_ADMIN", claims.get("role"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
