package com.hiremind.jobdescription.controller;

import com.hiremind.common.dto.ApiResponse;
import com.hiremind.jobdescription.dto.JobDescriptionRequest;
import com.hiremind.jobdescription.entity.JobDescription;
import com.hiremind.jobdescription.service.JobDescriptionService;
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
@RequestMapping("/api/v1/job-descriptions")
@RequiredArgsConstructor
@Slf4j
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobDescription>> createJobDescription(
        @Valid @RequestBody JobDescriptionRequest request,
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Creating job description for recruiter: {}", recruiterId);

        JobDescription jd = jobDescriptionService.createJobDescription(recruiterId, request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(jd, "Job description created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobDescription>>> getJobDescriptions(
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Fetching job descriptions for recruiter: {}", recruiterId);

        List<JobDescription> jds = jobDescriptionService.getRecruiterJobDescriptions(recruiterId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(jds, "Job descriptions retrieved successfully"));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<JobDescription>>> getActiveJobDescriptions(
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Fetching active job descriptions for recruiter: {}", recruiterId);

        List<JobDescription> jds = jobDescriptionService.getActiveJobDescriptions(recruiterId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(jds, "Active job descriptions retrieved successfully"));
    }

    @GetMapping("/{jdId}")
    public ResponseEntity<ApiResponse<JobDescription>> getJobDescription(
        @PathVariable UUID jdId,
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Fetching job description: {} for recruiter: {}", jdId, recruiterId);

        JobDescription jd = jobDescriptionService.getJobDescription(jdId, recruiterId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(jd, "Job description retrieved successfully"));
    }

    @PutMapping("/{jdId}")
    public ResponseEntity<ApiResponse<JobDescription>> updateJobDescription(
        @PathVariable UUID jdId,
        @Valid @RequestBody JobDescriptionRequest request,
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Updating job description: {} for recruiter: {}", jdId, recruiterId);

        JobDescription jd = jobDescriptionService.updateJobDescription(jdId, recruiterId, request);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(jd, "Job description updated successfully"));
    }

    @DeleteMapping("/{jdId}")
    public ResponseEntity<ApiResponse<Void>> deleteJobDescription(
        @PathVariable UUID jdId,
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Deleting job description: {} for recruiter: {}", jdId, recruiterId);

        jobDescriptionService.deleteJobDescription(jdId, recruiterId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success(null, "Job description deleted successfully"));
    }

    @PutMapping("/{jdId}/toggle-status")
    public ResponseEntity<ApiResponse<JobDescription>> toggleStatus(
        @PathVariable UUID jdId,
        @RequestParam("is_active") boolean isActive,
        Authentication authentication
    ) {
        UUID recruiterId = UUID.fromString(authentication.getName());
        log.info("Toggling job description status: {} - Active: {}", jdId, isActive);

        JobDescription jd = jobDescriptionService.toggleJobDescriptionStatus(jdId, recruiterId, isActive);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(jd, "Job description status updated successfully"));
    }
}
