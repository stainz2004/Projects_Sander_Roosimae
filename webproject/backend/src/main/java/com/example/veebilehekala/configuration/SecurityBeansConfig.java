package com.example.veebilehekala.configuration;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
public class SecurityBeansConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecretKey jwtKey() {
        // This generates new key at startup
        // All users need to log in again after server restart
        // But you don't have to think about where to store secret
        return Jwts.SIG.HS256.key().build();
    }
}
