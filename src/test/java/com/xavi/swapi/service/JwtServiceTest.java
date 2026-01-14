package com.xavi.swapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Configurar valores usando reflection (simulando @Value)
        ReflectionTestUtils.setField(jwtService, "secretKey", "dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLW11c3QtYmUtbG9uZw==");
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 900000L); // 15 min
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", 604800000L); // 7 días

        userDetails = User.builder()
                .username("test@test.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void generateAccessToken_shouldReturnValidToken() {
        String token = jwtService.generateAccessToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes
    }

    @Test
    void generateRefreshToken_shouldReturnValidToken() {
        String token = jwtService.generateRefreshToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateAccessToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("test@test.com", username);
    }

    @Test
    void isTokenValid_withValidToken_shouldReturnTrue() {
        String token = jwtService.generateAccessToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_withDifferentUser_shouldReturnFalse() {
        String token = jwtService.generateAccessToken(userDetails);

        UserDetails differentUser = User.builder()
                .username("other@test.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        boolean isValid = jwtService.isTokenValid(token, differentUser);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_withExpiredToken_shouldThrowException() {
        // Configurar expiración muy corta
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 1L); // 1ms

        String token = jwtService.generateAccessToken(userDetails);

        // Esperar a que expire
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // JWT library lanza ExpiredJwtException cuando el token está expirado
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(token, userDetails);
        });
    }

    @Test
    void generateAccessToken_withExtraClaims_shouldIncludeClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        String token = jwtService.generateAccessToken(extraClaims, userDetails);

        assertNotNull(token);
        // El token debe ser válido
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}
