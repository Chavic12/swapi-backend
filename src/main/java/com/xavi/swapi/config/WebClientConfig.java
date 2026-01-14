package com.xavi.swapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${swapi.base-url:https://www.swapi.tech/api}")
    private String swapiBaseUrl;

    @Bean
    public WebClient swapiWebClient() {
        return WebClient.builder()
                .baseUrl(swapiBaseUrl)
                .build();
    }
}
