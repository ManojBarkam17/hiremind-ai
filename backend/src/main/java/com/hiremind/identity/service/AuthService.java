package com.hiremind.identity.service;

import com.hiremind.common.exception.UnauthorizedException;
import com.hiremind.common.exception.ValidationException;
import com.hiremind.common.security.JwtTokenProvider;
import com.hiremind.identity.dto.LoginRequest;
import com.hiremind.identity.dto.LoginResponse;
import com.hiremind.identity.dto.RegisterRequest;
import com.hiremind.identity.entity.User;
import com.hiremind.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse register(RegisterRequest request) {
        // Validate email is not already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("email", "Email is already registered");
            throw new ValidationException("Registration validation failed", errors);
        }

        // Create new user
        User user = User.builder()
            .email(request.getEmail())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .role(request.getRoleEnum())
            .isActive(true)
            .emailVerified(false)
            .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());

        // Generate tokens
        String accessToken = jwtTokenProvider.generateToken(
            user.getId().toString(),
            user.getEmail(),
            user.getRole().name()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId().toString());

        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole(),
            accessToken,
            86400000L,
            refreshToken
        );
    }

    public LoginResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> {
                log.warn("Login attempt with non-existent email: {}", request.getEmail());
                return new UnauthorizedException("Invalid email or password");
            });

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Login attempt with wrong password for email: {}", request.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }

        // Check if user is active
        if (!user.getIsActive()) {
            throw new UnauthorizedException("User account is inactive");
        }

        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("User logged in successfully: {}", user.getEmail());

        // Generate tokens
        String accessToken = jwtTokenProvider.generateToken(
            user.getId().toString(),
            user.getEmail(),
            user.getRole().name()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId().toString());

        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole(),
            accessToken,
            86400000L,
            refreshToken
        );
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(java.util.UUID.fromString(userId))
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        String accessToken = jwtTokenProvider.generateToken(
            user.getId().toString(),
            user.getEmail(),
            user.getRole().name()
        );

        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole(),
            accessToken,
            86400000L,
            refreshToken
        );
    }
}
