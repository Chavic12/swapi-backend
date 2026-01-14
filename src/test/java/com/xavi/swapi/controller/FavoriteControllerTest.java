package com.xavi.swapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xavi.swapi.dto.FavoriteRequest;
import com.xavi.swapi.dto.FavoriteResponse;
import com.xavi.swapi.dto.PeopleDTO;
import com.xavi.swapi.entity.ResourceType;
import com.xavi.swapi.entity.Role;
import com.xavi.swapi.entity.User;
import com.xavi.swapi.exception.FavoriteAlreadyExistsException;
import com.xavi.swapi.exception.FavoriteNotFoundException;
import com.xavi.swapi.exception.GlobalExceptionHandler;
import com.xavi.swapi.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    private User testUser;
    private FavoriteResponse favoriteResponse;
    private PeopleDTO peopleDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .build();

        testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        peopleDTO = PeopleDTO.builder()
                .uid("1")
                .name("Luke Skywalker")
                .gender("male")
                .build();

        favoriteResponse = FavoriteResponse.builder()
                .id(1L)
                .resourceType(ResourceType.PEOPLE)
                .resourceId("1")
                .createdAt(LocalDateTime.now())
                .resource(peopleDTO)
                .build();
    }

    @Test
    void addFavorite_withValidRequest_shouldReturn201() throws Exception {
        FavoriteRequest request = FavoriteRequest.builder()
                .resourceType(ResourceType.PEOPLE)
                .resourceId("1")
                .build();

        when(favoriteService.addFavorite(any(User.class), any(FavoriteRequest.class)))
                .thenReturn(favoriteResponse);

        mockMvc.perform(post("/api/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.resourceType").value("PEOPLE"))
                .andExpect(jsonPath("$.resourceId").value("1"));

        verify(favoriteService).addFavorite(any(User.class), any(FavoriteRequest.class));
    }

    @Test
    void addFavorite_withExistingFavorite_shouldReturn409() throws Exception {
        FavoriteRequest request = FavoriteRequest.builder()
                .resourceType(ResourceType.PEOPLE)
                .resourceId("1")
                .build();

        when(favoriteService.addFavorite(any(User.class), any(FavoriteRequest.class)))
                .thenThrow(new FavoriteAlreadyExistsException("Este recurso ya esta en tus favoritos"));

        mockMvc.perform(post("/api/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getFavorites_withoutFilter_shouldReturn200() throws Exception {
        when(favoriteService.getFavorites(any(User.class), eq(null)))
                .thenReturn(List.of(favoriteResponse));

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].resourceType").value("PEOPLE"));

        verify(favoriteService).getFavorites(any(User.class), eq(null));
    }

    @Test
    void getFavorites_withTypeFilter_shouldReturn200() throws Exception {
        when(favoriteService.getFavorites(any(User.class), eq(ResourceType.PEOPLE)))
                .thenReturn(List.of(favoriteResponse));

        mockMvc.perform(get("/api/favorites")
                        .param("type", "PEOPLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resourceType").value("PEOPLE"));

        verify(favoriteService).getFavorites(any(User.class), eq(ResourceType.PEOPLE));
    }

    @Test
    void removeFavorite_withValidId_shouldReturn200() throws Exception {
        doNothing().when(favoriteService).removeFavorite(any(User.class), eq(1L));

        mockMvc.perform(delete("/api/favorites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Favorito eliminado exitosamente"));

        verify(favoriteService).removeFavorite(any(User.class), eq(1L));
    }

    @Test
    void removeFavorite_withInvalidId_shouldReturn404() throws Exception {
        doThrow(new FavoriteNotFoundException("Favorito no encontrado"))
                .when(favoriteService).removeFavorite(any(User.class), eq(999L));

        mockMvc.perform(delete("/api/favorites/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkFavorite_withExistingFavorite_shouldReturnTrue() throws Exception {
        when(favoriteService.isFavorite(any(User.class), eq(ResourceType.PEOPLE), eq("1")))
                .thenReturn(true);

        mockMvc.perform(get("/api/favorites/check")
                        .param("type", "PEOPLE")
                        .param("resourceId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkFavorite_withNonExistingFavorite_shouldReturnFalse() throws Exception {
        when(favoriteService.isFavorite(any(User.class), eq(ResourceType.PEOPLE), eq("999")))
                .thenReturn(false);

        mockMvc.perform(get("/api/favorites/check")
                        .param("type", "PEOPLE")
                        .param("resourceId", "999"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
