package com.truchi.vastragruh.service;

import com.truchi.vastragruh.dto.AuthenticationResponse;
import com.truchi.vastragruh.dto.LoginRequest;
import com.truchi.vastragruh.dto.LoginResponse;
import com.truchi.vastragruh.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

}