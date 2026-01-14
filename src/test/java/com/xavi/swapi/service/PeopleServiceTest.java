package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.PeopleDTO;
import com.xavi.swapi.dto.PeopleListItem;
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
class PeopleServiceTest {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private PeopleService peopleService;

    private SwapiListResponse<SwapiResultItem> listResponse;
    private SwapiDetailResponse<PeopleProperties> detailResponse;

    @BeforeEach
    void setUp() {
        SwapiResultItem item = new SwapiResultItem();
        item.setUid("1");
        item.setName("Luke Skywalker");
        item.setUrl("https://swapi.tech/api/people/1");

        listResponse = new SwapiListResponse<>();
        listResponse.setMessage("ok");
        listResponse.setTotalRecords(82);
        listResponse.setTotalPages(9);
        listResponse.setResults(List.of(item));
        listResponse.setNext("page=2");

        PeopleProperties props = new PeopleProperties();
        props.setName("Luke Skywalker");
        props.setGender("male");
        props.setHeight("172");
        props.setMass("77");
        props.setBirthYear("19BBY");

        SwapiResult<PeopleProperties> result = new SwapiResult<>();
        result.setUid("1");
        result.setProperties(props);

        detailResponse = new SwapiDetailResponse<>();
        detailResponse.setMessage("ok");
        detailResponse.setResult(result);
    }

    @Test
    void getAll_shouldReturnPageResponse() {
        when(swapiClient.getPeople(1, 10)).thenReturn(listResponse);

        PageResponse<PeopleListItem> response = peopleService.getAll(1, 10, null);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Luke Skywalker", response.getContent().get(0).getName());
        assertEquals(82, response.getTotalRecords());
        assertEquals(9, response.getTotalPages());
        assertTrue(response.isHasNext());
        assertFalse(response.isHasPrevious());

        verify(swapiClient).getPeople(1, 10);
    }

    @Test
    void getAll_withNameFilter_shouldSearchByName() {
        when(swapiClient.searchPeopleByName("luke", 1, 10)).thenReturn(listResponse);

        PageResponse<PeopleListItem> response = peopleService.getAll(1, 10, "luke");

        assertNotNull(response);
        assertEquals(1, response.getContent().size());

        verify(swapiClient).searchPeopleByName("luke", 1, 10);
        verify(swapiClient, never()).getPeople(anyInt(), anyInt());
    }

    @Test
    void getById_shouldReturnPeopleDTO() {
        when(swapiClient.getPeopleById("1")).thenReturn(detailResponse);

        PeopleDTO dto = peopleService.getById("1");

        assertNotNull(dto);
        assertEquals("1", dto.getUid());
        assertEquals("Luke Skywalker", dto.getName());
        assertEquals("male", dto.getGender());
        assertEquals("172", dto.getHeight());
        assertEquals("77", dto.getMass());
        assertEquals("19BBY", dto.getBirthYear());

        verify(swapiClient).getPeopleById("1");
    }
}
