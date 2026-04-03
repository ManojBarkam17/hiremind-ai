package com.hiremind.analysis.controller;

import com.hiremind.analysis.dto.AnalysisRequest;
import com.hiremind.analysis.dto.AnalysisResponse;
import com.hiremind.analysis.service.AnalysisService;
import com.hiremind.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
@Slf4j
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnalysisResponse>> analyzeResume(
        @Valid @RequestBody AnalysisRequest request,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Analyzing resume: {} for user: {}", request.getResumeId(), userId);

        AnalysisResponse response = analysisService.analyzeResume(userId, request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Resume analysis completed successfully"));
    }

    @GetMapping("/{analysisId}")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysis(
        @PathVariable UUID analysisId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching analysis: {} for user: {}", analysisId, userId);

        AnalysisResponse response = analysisService.getAnalysis(analysisId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(response, "Analysis retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> getUserAnalyses(
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching all analyses for user: {}", userId);

        List<AnalysisResponse> responses = analysisService.getUserAnalyses(userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(responses, "Analyses retrieved successfully"));
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> getResumeAnalyses(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching analyses for resume: {} and user: {}", resumeId, userId);

        List<AnalysisResponse> responses = analysisService.getResumeAnalyses(resumeId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(responses, "Resume analyses retrieved successfully"));
    }
}
