package com.circuit.breaker.service;

import com.circuit.breaker.model.UserDetails;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;

@Service
public class UserDetailService implements UserDetail {
    private static final Logger log = LoggerFactory.getLogger(UserDetailService.class);
    @Autowired
    RestClient restClient;

    @Override

    public UserDetails fetchUser(String username) {

        UserDetails userDetails = null;
        //try {
            userDetails = restClient.get().uri("/api/v1/user/" + username)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(UserDetails.class);
       /* }catch (Exception ex)
        {
            log.error("Getting Exception:{}",ex.getMessage());
            //throw new RuntimeException("Getting Exception");
        }*/

        return userDetails;
    }

}
