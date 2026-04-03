package com.hiremind.optimization.controller;

import com.hiremind.common.dto.ApiResponse;
import com.hiremind.optimization.dto.OptimizationRequest;
import com.hiremind.optimization.dto.OptimizationResponse;
import com.hiremind.optimization.service.OptimizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/optimizations")
@RequiredArgsConstructor
@Slf4j
public class OptimizationController {

    private final OptimizationService optimizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OptimizationResponse>> optimizeResume(
        @Valid @RequestBody OptimizationRequest request,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Optimizing resume: {} for user: {}", request.getResumeId(), userId);

        OptimizationResponse response = optimizationService.optimizeResume(userId, request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Resume optimization completed successfully"));
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<ApiResponse<List<OptimizationResponse>>> getOptimizationVersions(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching optimization versions for resume: {} and user: {}", resumeId, userId);

        List<OptimizationResponse> responses = optimizationService.getOptimizationVersions(resumeId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(responses, "Optimization versions retrieved successfully"));
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<ApiResponse<OptimizationResponse>> getOptimizationVersion(
        @PathVariable UUID versionId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching optimization version: {} for user: {}", versionId, userId);

        OptimizationResponse response = optimizationService.getOptimizationVersion(versionId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(response, "Optimization version retrieved successfully"));
    }

    @PutMapping("/{versionId}/activate")
    public ResponseEntity<ApiResponse<OptimizationResponse>> activateVersion(
        @PathVariable UUID versionId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Activating optimization version: {} for user: {}", versionId, userId);

        OptimizationResponse response = optimizationService.activateVersion(versionId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(response, "Optimization version activated successfully"));
    }

    @GetMapping("/resume/{resumeId}/active")
    public ResponseEntity<ApiResponse<OptimizationResponse>> getActiveVersion(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching active optimization version for resume: {} and user: {}", resumeId, userId);

        OptimizationResponse response = optimizationService.getActiveVersion(resumeId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(response, "Active optimization version retrieved successfully"));
    }
}
