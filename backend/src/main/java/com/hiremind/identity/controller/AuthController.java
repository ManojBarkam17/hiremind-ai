package com.hiremind.identity.controller;

import com.hiremind.common.dto.ApiResponse;
import com.hiremind.identity.dto.LoginRequest;
import com.hiremind.identity.dto.LoginResponse;
import com.hiremind.identity.dto.RegisterRequest;
import com.hiremind.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for email: {}", request.getEmail());
        LoginResponse response = authService.register(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(response, "Login successful"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = extractToken(authHeader);
        log.debug("Refreshing token");
        LoginResponse response = authService.refreshToken(token);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(response, "Token refreshed successfully"));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        Map<String, String> data = new HashMap<>();
        data.put("status", "healthy");
        data.put("service", "HireMind AI - Auth Service");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(data, "Auth service is healthy"));
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
