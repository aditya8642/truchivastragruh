package com.truchi.vastragruh.controller;

import com.truchi.vastragruh.dto.*;
import com.truchi.vastragruh.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(
            @Valid @RequestBody RegisterRequest request) {

        return ApiResponse.<String>builder()
                .success(true)
                .message(authService.register(request))
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }


    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(
            @RequestBody LoginRequest request) {

        return ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Login Successful")
                .data(authService.login(request))
                .timestamp(LocalDateTime.now())
                .build();
    }

}