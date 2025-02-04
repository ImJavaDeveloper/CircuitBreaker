package com.circuit.breaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry registry) {

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig());
        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("myService");
        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {
                    log.info("Circuit Breaker State Transition: " + event.getStateTransition());
                })
                .onError(event -> {
                    log.info("Circuit Breaker Error: " + event.getThrowable().getMessage());
                });
        return circuitBreaker;
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        CircuitBreakerConfig circuitBreakerConfig=
        CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
               // .recordException(e -> INTERNAL_SERVER_ERROR
                 //       .equals(getResponse().getStatus()))
                //.recordExceptions(IOException.class, TimeoutException.class)
                //.ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreakerWithDefaultConfig =
                circuitBreakerRegistry.circuitBreaker("myService");
        return circuitBreakerConfig;
    }

}
