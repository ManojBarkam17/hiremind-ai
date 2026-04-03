package com.hiremind.export.controller;

import com.hiremind.common.dto.ApiResponse;
import com.hiremind.export.service.ExportService;
import com.hiremind.resume.entity.Resume;
import com.hiremind.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/export")
@RequiredArgsConstructor
@Slf4j
public class ExportController {

    private final ExportService exportService;
    private final ResumeService resumeService;

    @GetMapping("/resume/{resumeId}/plain-text")
    public ResponseEntity<?> exportAsPlainText(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Exporting resume {} as plain text for user: {}", resumeId, userId);

        Resume resume = resumeService.getResume(resumeId, userId);
        String content = exportService.exportAsPlainText(resume);

        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename(resume.getFileName() + ".txt")
                    .build()
                    .toString())
            .body(content);
    }

    @GetMapping("/resume/{resumeId}/markdown")
    public ResponseEntity<?> exportAsMarkdown(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Exporting resume {} as markdown for user: {}", resumeId, userId);

        Resume resume = resumeService.getResume(resumeId, userId);
        String content = exportService.exportAsMarkdown(resume, null);

        return ResponseEntity.ok()
            .contentType(MediaType.valueOf("text/markdown"))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename(resume.getFileName() + ".md")
                    .build()
                    .toString())
            .body(content);
    }

    @GetMapping("/resume/{resumeId}/json")
    public ResponseEntity<?> exportAsJSON(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Exporting resume {} as JSON for user: {}", resumeId, userId);

        Resume resume = resumeService.getResume(resumeId, userId);
        String content = exportService.exportAsJSON(resume, null);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename(resume.getFileName() + ".json")
                    .build()
                    .toString())
            .body(content);
    }

    @GetMapping("/resume/{resumeId}/ats-optimized")
    public ResponseEntity<?> exportATSOptimized(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Exporting resume {} as ATS-optimized for user: {}", resumeId, userId);

        Resume resume = resumeService.getResume(resumeId, userId);
        String content = exportService.exportAsASTOptimized(resume, "");

        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename("ATS_" + resume.getFileName() + ".txt")
                    .build()
                    .toString())
            .body(content);
    }

    @PostMapping("/resume/{resumeId}/formats")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAvailableFormats(
        @PathVariable UUID resumeId,
        Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("Fetching available export formats for resume {} and user: {}", resumeId, userId);

        // Verify user has access
        resumeService.getResume(resumeId, userId);

        Map<String, String> formats = new HashMap<>();
        formats.put("plain_text", "/api/v1/export/resume/" + resumeId + "/plain-text");
        formats.put("markdown", "/api/v1/export/resume/" + resumeId + "/markdown");
        formats.put("json", "/api/v1/export/resume/" + resumeId + "/json");
        formats.put("ats_optimized", "/api/v1/export/resume/" + resumeId + "/ats-optimized");

        return ResponseEntity.ok()
            .body(ApiResponse.success(formats, "Available export formats retrieved successfully"));
    }
}
