package com.xavi.swapi.service;

import com.xavi.swapi.dto.AuthResponse;
import com.xavi.swapi.dto.LoginRequest;
import com.xavi.swapi.dto.RefreshTokenRequest;
import com.xavi.swapi.dto.RegisterRequest;
import com.xavi.swapi.entity.Role;
import com.xavi.swapi.entity.User;
import com.xavi.swapi.exception.EmailAlreadyExistsException;
import com.xavi.swapi.exception.InvalidTokenException;
import com.xavi.swapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        registerRequest = new RegisterRequest("test@test.com", "password123");
        loginRequest = new LoginRequest("test@test.com", "password123");
    }

    @Test
    void register_withNewEmail_shouldReturnTokens() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(userRepository).existsByEmail("test@test.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_withExistingEmail_shouldThrowException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository).existsByEmail("test@test.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_withValidCredentials_shouldReturnTokens() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void login_withInvalidCredentials_shouldThrowException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void refreshToken_withValidToken_shouldReturnNewAccessToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");

        when(jwtService.extractUsername(anyString())).thenReturn("test@test.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("new-access-token");

        AuthResponse response = authService.refreshToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("valid-refresh-token", response.getRefreshToken());

        verify(jwtService).extractUsername("valid-refresh-token");
        verify(jwtService).isTokenValid("valid-refresh-token", testUser);
    }

    @Test
    void refreshToken_withInvalidToken_shouldThrowException() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-refresh-token");

        when(jwtService.extractUsername(anyString())).thenReturn("test@test.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> {
            authService.refreshToken(request);
        });

        verify(jwtService).isTokenValid("invalid-refresh-token", testUser);
    }

    @Test
    void refreshToken_withNonExistentUser_shouldThrowException() {
        RefreshTokenRequest request = new RefreshTokenRequest("some-token");

        when(jwtService.extractUsername(anyString())).thenReturn("nonexistent@test.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            authService.refreshToken(request);
        });
    }
}
