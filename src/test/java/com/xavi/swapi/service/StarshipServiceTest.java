package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.StarshipDTO;
import com.xavi.swapi.dto.StarshipListItem;
import com.xavi.swapi.dto.swapi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StarshipServiceTest {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private StarshipService starshipService;

    private SwapiListResponse<SwapiResultItem> listResponse;
    private SwapiDetailResponse<StarshipProperties> detailResponse;

    @BeforeEach
    void setUp() {
        SwapiResultItem item = new SwapiResultItem();
        item.setUid("2");
        item.setName("CR90 corvette");
        item.setUrl("https://swapi.tech/api/starships/2");

        listResponse = new SwapiListResponse<>();
        listResponse.setMessage("ok");
        listResponse.setTotalRecords(36);
        listResponse.setTotalPages(4);
        listResponse.setResults(List.of(item));

        StarshipProperties props = new StarshipProperties();
        props.setName("CR90 corvette");
        props.setModel("CR90 corvette");
        props.setManufacturer("Corellian Engineering Corporation");
        props.setStarshipClass("corvette");
        props.setLength("150");

        SwapiResult<StarshipProperties> result = new SwapiResult<>();
        result.setUid("2");
        result.setProperties(props);

        detailResponse = new SwapiDetailResponse<>();
        detailResponse.setMessage("ok");
        detailResponse.setResult(result);
    }

    @Test
    void getAll_shouldReturnPageResponse() {
        when(swapiClient.getStarships(1, 10)).thenReturn(listResponse);

        PageResponse<StarshipListItem> response = starshipService.getAll(1, 10, null);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("CR90 corvette", response.getContent().get(0).getName());

        verify(swapiClient).getStarships(1, 10);
    }

    @Test
    void getById_shouldReturnStarshipDTO() {
        when(swapiClient.getStarshipById("2")).thenReturn(detailResponse);

        StarshipDTO dto = starshipService.getById("2");

        assertNotNull(dto);
        assertEquals("2", dto.getUid());
        assertEquals("CR90 corvette", dto.getName());
        assertEquals("Corellian Engineering Corporation", dto.getManufacturer());

        verify(swapiClient).getStarshipById("2");
    }
}
