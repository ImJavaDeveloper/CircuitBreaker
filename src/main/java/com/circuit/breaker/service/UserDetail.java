package com.circuit.breaker.service;

import com.circuit.breaker.model.UserDetails;

public interface UserDetail {

    UserDetails fetchUser(String username);
}
