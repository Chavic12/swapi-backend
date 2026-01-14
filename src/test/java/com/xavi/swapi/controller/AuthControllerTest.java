package com.xavi.swapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavi.swapi.dto.AuthResponse;
import com.xavi.swapi.dto.LoginRequest;
import com.xavi.swapi.dto.RefreshTokenRequest;
import com.xavi.swapi.dto.RegisterRequest;
import com.xavi.swapi.exception.EmailAlreadyExistsException;
import com.xavi.swapi.exception.GlobalExceptionHandler;
import com.xavi.swapi.exception.InvalidTokenException;
import com.xavi.swapi.service.AuthService;
import com.xavi.swapi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        authResponse = AuthResponse.builder()
                .accessToken("access-token-123")
                .refreshToken("refresh-token-456")
                .build();
    }

    @Test
    void register_withValidRequest_shouldReturn200() throws Exception {
        RegisterRequest request = new RegisterRequest("test@test.com", "Password@123");

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-456"));
    }

    @Test
    void register_withExistingEmail_shouldReturn409() throws Exception {
        RegisterRequest request = new RegisterRequest("existing@test.com", "Password@123");

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("El email ya está registrado"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_withValidCredentials_shouldReturn200() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "Password@123");

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-456"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "wrongpassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_withValidToken_shouldReturn200() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");

        AuthResponse refreshResponse = AuthResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("valid-refresh-token")
                .build();

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(refreshResponse);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("valid-refresh-token"));
    }

    @Test
    void refresh_withInvalidToken_shouldReturn401() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-refresh-token");

        when(authService.refreshToken(any(RefreshTokenRequest.class)))
                .thenThrow(new InvalidTokenException("Refresh token inválido"));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
