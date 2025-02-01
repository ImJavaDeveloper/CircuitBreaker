package com.circuit.breaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CircuitBreakerConfig {

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry registry) {
        CircuitBreaker circuitBreaker = registry.circuitBreaker("myService");
        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {
                    log.info("Circuit Breaker State Transition: " + event.getStateTransition());
                })
                .onError(event -> {
                    log.info("Circuit Breaker Error: " + event.getThrowable().getMessage());
                });
        return circuitBreaker;
    }
}
