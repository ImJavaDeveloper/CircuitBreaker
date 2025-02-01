package com.circuit.breaker.controller;

import com.circuit.breaker.model.UserDetails;
import com.circuit.breaker.service.UserDetail;
import com.circuit.breaker.service.UserDetailService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    @Autowired
    UserDetailService userDetailService;
    @GetMapping("/userDetail/{username}")
    @CircuitBreaker(name = "myService", fallbackMethod = "fallbackResponse")
    public ResponseEntity<UserDetails> getUserDetail(@PathVariable String username)
    {
        return new ResponseEntity<>(userDetailService.fetchUser(username), HttpStatus.ACCEPTED);
    }


    public ResponseEntity<String> fallbackResponse(String username, Throwable ex) {
        log.info(ex.getMessage());
        log.info(ex.getLocalizedMessage());
        log.info(ex.fillInStackTrace().toString());
        return ResponseEntity.ok("Fallback: Service is down. Cannot retrieve data for ID: " + username);
    }

    @CircuitBreaker(name = "myService", fallbackMethod = "fallbackResponse")
    @GetMapping("/data")
    public String fetchData() {
        // This simulates a failing call
        throw new RuntimeException("Simulated failure");
    }

    public String fallbackResponse(Throwable t) {
        return "Fallback due to: " + t.getMessage();
    }
}
