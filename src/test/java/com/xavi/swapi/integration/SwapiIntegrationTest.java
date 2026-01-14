package com.xavi.swapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.xavi.swapi.dto.AuthResponse;
import com.xavi.swapi.dto.RegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwapiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static WireMockServer wireMockServer;
    private String accessToken;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("swapi.base-url", () -> "http://localhost:8089");
    }

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        wireMockServer.resetAll();

        RegisterRequest registerRequest = new RegisterRequest(
                "test" + System.currentTimeMillis() + "@test.com",
                "Password@123"
        );

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthResponse.class
        );
        accessToken = authResponse.getAccessToken();
    }

    @Test
    void getPeople_shouldReturnPaginatedList() throws Exception {
        stubFor(WireMock.get(urlPathEqualTo("/people"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "message": "ok",
                                    "total_records": 82,
                                    "total_pages": 9,
                                    "next": "?page=2&limit=10",
                                    "previous": null,
                                    "results": [
                                        {"uid": "1", "name": "Luke Skywalker", "url": "https://swapi.tech/api/people/1"}
                                    ]
                                }
                                """)));

        mockMvc.perform(get("/api/people")
                        .param("page", "1")
                        .param("limit", "10")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Luke Skywalker"))
                .andExpect(jsonPath("$.totalRecords").value(82));
    }

    @Test
    void getPeopleById_shouldReturnPeopleDetail() throws Exception {
        stubFor(WireMock.get(urlPathEqualTo("/people/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "message": "ok",
                                    "result": {
                                        "properties": {
                                            "name": "Luke Skywalker",
                                            "gender": "male",
                                            "height": "172",
                                            "mass": "77",
                                            "birth_year": "19BBY",
                                            "skin_color": "fair",
                                            "hair_color": "blond",
                                            "eye_color": "blue",
                                            "homeworld": "https://swapi.tech/api/planets/1"
                                        },
                                        "uid": "1",
                                        "description": "A person"
                                    }
                                }
                                """)));

        mockMvc.perform(get("/api/people/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luke Skywalker"))
                .andExpect(jsonPath("$.birthYear").value("19BBY"));
    }

    @Test
    void getEndpoint_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/people"))
                .andExpect(status().isUnauthorized());
    }
}
