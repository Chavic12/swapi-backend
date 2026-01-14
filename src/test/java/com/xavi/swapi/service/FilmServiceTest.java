package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.FilmDTO;
import com.xavi.swapi.dto.PageResponse;
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
class FilmServiceTest {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private FilmService filmService;

    private SwapiListResponse<FilmResultItem> listResponse;
    private SwapiDetailResponse<FilmProperties> detailResponse;

    @BeforeEach
    void setUp() {
        FilmProperties props = new FilmProperties();
        props.setTitle("A New Hope");
        props.setEpisodeId(4);
        props.setDirector("George Lucas");
        props.setProducer("Gary Kurtz");
        props.setReleaseDate("1977-05-25");
        props.setUrl("https://swapi.tech/api/films/1");

        FilmResultItem item = new FilmResultItem();
        item.setUid("1");
        item.setProperties(props);

        listResponse = new SwapiListResponse<>();
        listResponse.setMessage("ok");
        listResponse.setTotalRecords(6);
        listResponse.setTotalPages(1);
        listResponse.setResults(List.of(item));

        SwapiResult<FilmProperties> result = new SwapiResult<>();
        result.setUid("1");
        result.setProperties(props);

        detailResponse = new SwapiDetailResponse<>();
        detailResponse.setMessage("ok");
        detailResponse.setResult(result);
    }

    @Test
    void getAll_shouldReturnPageResponse() {
        when(swapiClient.getFilms(1, 10)).thenReturn(listResponse);

        PageResponse<FilmDTO> response = filmService.getAll(1, 10, null);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("A New Hope", response.getContent().get(0).getTitle());
        assertEquals(6, response.getTotalRecords());

        verify(swapiClient).getFilms(1, 10);
    }

    @Test
    void getById_shouldReturnFilmDTO() {
        when(swapiClient.getFilmById("1")).thenReturn(detailResponse);

        FilmDTO dto = filmService.getById("1");

        assertNotNull(dto);
        assertEquals("1", dto.getUid());
        assertEquals("A New Hope", dto.getTitle());
        assertEquals(4, dto.getEpisodeId());
        assertEquals("George Lucas", dto.getDirector());

        verify(swapiClient).getFilmById("1");
    }
}
