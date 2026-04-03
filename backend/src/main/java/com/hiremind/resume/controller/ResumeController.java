package com.hiremind.resume.controller;

import com.hiremind.common.dto.ApiResponse;
import com.hiremind.resume.dto.ResumeUploadResponse;
import com.hiremind.resume.entity.Resume;
import com.hiremind.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ResumeUploadResponse>> uploadResume(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "set_as_primary", defaultValue = "false") boolean setAsPrimary,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Resume upload request for user: {}", userId);

        ResumeUploadResponse response = resumeService.uploadResume(userId, file, setAsPrimary);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Resume uploaded successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Resume>>> getUserResumes(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching resumes for user: {}", userId);

        List<Resume> resumes = resumeService.getUserResumes(userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(resumes, "Resumes retrieved successfully"));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<Resume>> getResume(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching resume: {} for user: {}", resumeId, userId);

        Resume resume = resumeService.getResume(resumeId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(resume, "Resume retrieved successfully"));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Deleting resume: {} for user: {}", resumeId, userId);

        resumeService.deleteResume(resumeId, userId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success(null, "Resume deleted successfully"));
    }

    @PutMapping("/{resumeId}/set-primary")
    public ResponseEntity<ApiResponse<Resume>> setPrimaryResume(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Setting resume as primary: {} for user: {}", resumeId, userId);

        Resume resume = resumeService.setPrimaryResume(resumeId, userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(resume, "Resume set as primary successfully"));
    }

    @GetMapping("/primary/current")
    public ResponseEntity<ApiResponse<Resume>> getPrimaryResume(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching primary resume for user: {}", userId);

        Resume resume = resumeService.getPrimaryResume(userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(resume, "Primary resume retrieved successfully"));
    }
}
