package com.circuit.breaker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Autowired
    RestErrorHandler restErrorHandler;
    @Bean
    public RestClient restClient()
    {
        return RestClient.builder()
                .baseUrl("http://localhost:8083")
                .defaultStatusHandler(restErrorHandler)
                .build();
    }
}
