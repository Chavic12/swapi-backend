package com.xavi.swapi.service;

import com.xavi.swapi.dto.*;
import com.xavi.swapi.entity.Favorite;
import com.xavi.swapi.entity.ResourceType;
import com.xavi.swapi.entity.Role;
import com.xavi.swapi.entity.User;
import com.xavi.swapi.exception.FavoriteAlreadyExistsException;
import com.xavi.swapi.exception.FavoriteNotFoundException;
import com.xavi.swapi.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private PeopleService peopleService;

    @Mock
    private FilmService filmService;

    @Mock
    private StarshipService starshipService;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private FavoriteService favoriteService;

    private User testUser;
    private Favorite testFavorite;
    private FavoriteRequest favoriteRequest;
    private PeopleDTO peopleDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        testFavorite = Favorite.builder()
                .id(1L)
                .user(testUser)
                .resourceType(ResourceType.PEOPLE)
                .resourceId("1")
                .createdAt(LocalDateTime.now())
                .build();

        favoriteRequest = FavoriteRequest.builder()
                .resourceType(ResourceType.PEOPLE)
                .resourceId("1")
                .build();

        peopleDTO = PeopleDTO.builder()
                .uid("1")
                .name("Luke Skywalker")
                .gender("male")
                .build();
    }

    @Test
    void addFavorite_withNewResource_shouldReturnFavoriteResponse() {
        when(favoriteRepository.existsByUserAndResourceTypeAndResourceId(
                testUser, ResourceType.PEOPLE, "1")).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(testFavorite);
        when(peopleService.getById("1")).thenReturn(peopleDTO);

        FavoriteResponse response = favoriteService.addFavorite(testUser, favoriteRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(ResourceType.PEOPLE, response.getResourceType());
        assertEquals("1", response.getResourceId());

        verify(favoriteRepository).existsByUserAndResourceTypeAndResourceId(testUser, ResourceType.PEOPLE, "1");
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_withExistingResource_shouldThrowException() {
        when(favoriteRepository.existsByUserAndResourceTypeAndResourceId(
                testUser, ResourceType.PEOPLE, "1")).thenReturn(true);

        assertThrows(FavoriteAlreadyExistsException.class, () -> {
            favoriteService.addFavorite(testUser, favoriteRequest);
        });

        verify(favoriteRepository).existsByUserAndResourceTypeAndResourceId(testUser, ResourceType.PEOPLE, "1");
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    void getFavorites_withoutFilter_shouldReturnAllFavorites() {
        when(favoriteRepository.findByUserOrderByCreatedAtDesc(testUser))
                .thenReturn(List.of(testFavorite));
        when(peopleService.getById("1")).thenReturn(peopleDTO);

        List<FavoriteResponse> response = favoriteService.getFavorites(testUser, null);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ResourceType.PEOPLE, response.get(0).getResourceType());

        verify(favoriteRepository).findByUserOrderByCreatedAtDesc(testUser);
    }

    @Test
    void getFavorites_withFilter_shouldReturnFilteredFavorites() {
        when(favoriteRepository.findByUserAndResourceTypeOrderByCreatedAtDesc(testUser, ResourceType.PEOPLE))
                .thenReturn(List.of(testFavorite));
        when(peopleService.getById("1")).thenReturn(peopleDTO);

        List<FavoriteResponse> response = favoriteService.getFavorites(testUser, ResourceType.PEOPLE);

        assertNotNull(response);
        assertEquals(1, response.size());

        verify(favoriteRepository).findByUserAndResourceTypeOrderByCreatedAtDesc(testUser, ResourceType.PEOPLE);
    }

    @Test
    void removeFavorite_withValidId_shouldDeleteFavorite() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(testFavorite));

        assertDoesNotThrow(() -> favoriteService.removeFavorite(testUser, 1L));

        verify(favoriteRepository).findById(1L);
        verify(favoriteRepository).delete(testFavorite);
    }

    @Test
    void removeFavorite_withInvalidId_shouldThrowException() {
        when(favoriteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FavoriteNotFoundException.class, () -> {
            favoriteService.removeFavorite(testUser, 999L);
        });

        verify(favoriteRepository).findById(999L);
        verify(favoriteRepository, never()).delete(any(Favorite.class));
    }

    @Test
    void removeFavorite_withOtherUserFavorite_shouldThrowException() {
        User otherUser = User.builder().id(2L).email("other@test.com").build();
        Favorite otherUserFavorite = Favorite.builder()
                .id(2L)
                .user(otherUser)
                .resourceType(ResourceType.PEOPLE)
                .resourceId("1")
                .build();

        when(favoriteRepository.findById(2L)).thenReturn(Optional.of(otherUserFavorite));

        assertThrows(FavoriteNotFoundException.class, () -> {
            favoriteService.removeFavorite(testUser, 2L);
        });

        verify(favoriteRepository, never()).delete(any(Favorite.class));
    }

    @Test
    void isFavorite_withExistingFavorite_shouldReturnTrue() {
        when(favoriteRepository.existsByUserAndResourceTypeAndResourceId(
                testUser, ResourceType.PEOPLE, "1")).thenReturn(true);

        boolean result = favoriteService.isFavorite(testUser, ResourceType.PEOPLE, "1");

        assertTrue(result);
        verify(favoriteRepository).existsByUserAndResourceTypeAndResourceId(testUser, ResourceType.PEOPLE, "1");
    }

    @Test
    void isFavorite_withNonExistingFavorite_shouldReturnFalse() {
        when(favoriteRepository.existsByUserAndResourceTypeAndResourceId(
                testUser, ResourceType.PEOPLE, "999")).thenReturn(false);

        boolean result = favoriteService.isFavorite(testUser, ResourceType.PEOPLE, "999");

        assertFalse(result);
    }
}
