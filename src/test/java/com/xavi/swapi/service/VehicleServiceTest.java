package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.VehicleDTO;
import com.xavi.swapi.dto.VehicleListItem;
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
class VehicleServiceTest {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private VehicleService vehicleService;

    private SwapiListResponse<SwapiResultItem> listResponse;
    private SwapiDetailResponse<VehicleProperties> detailResponse;

    @BeforeEach
    void setUp() {
        SwapiResultItem item = new SwapiResultItem();
        item.setUid("4");
        item.setName("Sand Crawler");
        item.setUrl("https://swapi.tech/api/vehicles/4");

        listResponse = new SwapiListResponse<>();
        listResponse.setMessage("ok");
        listResponse.setTotalRecords(39);
        listResponse.setTotalPages(4);
        listResponse.setResults(List.of(item));

        VehicleProperties props = new VehicleProperties();
        props.setName("Sand Crawler");
        props.setModel("Digger Crawler");
        props.setManufacturer("Corellia Mining Corporation");
        props.setVehicleClass("wheeled");
        props.setLength("36.8");

        SwapiResult<VehicleProperties> result = new SwapiResult<>();
        result.setUid("4");
        result.setProperties(props);

        detailResponse = new SwapiDetailResponse<>();
        detailResponse.setMessage("ok");
        detailResponse.setResult(result);
    }

    @Test
    void getAll_shouldReturnPageResponse() {
        when(swapiClient.getVehicles(1, 10)).thenReturn(listResponse);

        PageResponse<VehicleListItem> response = vehicleService.getAll(1, 10, null);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Sand Crawler", response.getContent().get(0).getName());

        verify(swapiClient).getVehicles(1, 10);
    }

    @Test
    void getById_shouldReturnVehicleDTO() {
        when(swapiClient.getVehicleById("4")).thenReturn(detailResponse);

        VehicleDTO dto = vehicleService.getById("4");

        assertNotNull(dto);
        assertEquals("4", dto.getUid());
        assertEquals("Sand Crawler", dto.getName());
        assertEquals("Corellia Mining Corporation", dto.getManufacturer());

        verify(swapiClient).getVehicleById("4");
    }
}
