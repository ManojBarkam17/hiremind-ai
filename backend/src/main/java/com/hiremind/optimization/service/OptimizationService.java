package com.hiremind.optimization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiremind.ai.service.ResumeRewriterService;
import com.hiremind.common.exception.ResourceNotFoundException;
import com.hiremind.optimization.dto.ChangeLog;
import com.hiremind.optimization.dto.OptimizationRequest;
import com.hiremind.optimization.dto.OptimizationResponse;
import com.hiremind.optimization.entity.OptimizationVersion;
import com.hiremind.optimization.repository.OptimizationVersionRepository;
import com.hiremind.resume.entity.Resume;
import com.hiremind.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OptimizationService {

    private final OptimizationVersionRepository optimizationVersionRepository;
    private final ResumeService resumeService;
    private final ResumeRewriterService resumeRewriterService;
    private final ObjectMapper objectMapper;

    public OptimizationResponse optimizeResume(UUID userId, OptimizationRequest request) {
        // Get resume
        Resume resume = resumeService.getResume(request.getResumeId(), userId);

        String improvementFocus = request.getImprovementFocus() != null ? request.getImprovementFocus() : "COMPREHENSIVE";

        String optimizedContent;
        List<ChangeLog> changeLogs = new ArrayList<>();
        double improvementScore;

        if ("KEYWORD_MATCHING".equalsIgnoreCase(improvementFocus) && request.getTargetKeywords() != null) {
            optimizedContent = resumeRewriterService.optimizeForKeywords(
                resume.getRawText(),
                request.getTargetKeywords()
            );
            changeLogs.add(ChangeLog.builder()
                .changeType("ADDED")
                .section("Skills")
                .reason("Added target keywords for better ATS matching")
                .impact("Improved keyword density and ATS compatibility")
                .build());
            improvementScore = 15.0;
        } else {
            // COMPREHENSIVE or ATS_OPTIMIZATION
            Map<String, Object> improvements = resumeRewriterService.suggestImprovements(resume.getRawText());
            optimizedContent = (String) improvements.getOrDefault("improved_version", resume.getRawText());

            @SuppressWarnings("unchecked")
            List<Map<String, String>> verbSuggestions = (List<Map<String, String>>) improvements.get("weak_verb_suggestions");
            for (Map<String, String> suggestion : verbSuggestions) {
                changeLogs.add(ChangeLog.builder()
                    .changeType("MODIFIED")
                    .section("Experience")
                    .originalText(suggestion.get("weak"))
                    .newText(suggestion.get("replacement"))
                    .reason("Strengthened action verb")
                    .impact(suggestion.get("impact"))
                    .build());
            }

            improvementScore = 20.0;
        }

        // Get next version number
        List<OptimizationVersion> existingVersions = optimizationVersionRepository.findByResumeIdOrderByVersionNumberDesc(request.getResumeId());
        int nextVersion = existingVersions.isEmpty() ? 1 : existingVersions.get(0).getVersionNumber() + 1;

        // Create optimization version
        OptimizationVersion version = OptimizationVersion.builder()
            .resumeId(request.getResumeId())
            .userId(userId)
            .versionNumber(nextVersion)
            .optimizedContent(optimizedContent)
            .changelog(serializeChangeLog(changeLogs))
            .improvementFocus(improvementFocus)
            .improvementScore(improvementScore)
            .isActive(false)
            .build();

        version = optimizationVersionRepository.save(version);
        log.info("Optimization version created: {} for resume: {}", version.getId(), request.getResumeId());

        return OptimizationResponse.builder()
            .versionId(version.getId())
            .resumeId(version.getResumeId())
            .versionNumber(version.getVersionNumber())
            .optimizedContent(version.getOptimizedContent())
            .improvementFocus(version.getImprovementFocus())
            .improvementScore(version.getImprovementScore())
            .changelog(changeLogs)
            .isActive(version.getIsActive())
            .createdAt(version.getCreatedAt())
            .message("Resume optimization completed successfully")
            .build();
    }

    public List<OptimizationResponse> getOptimizationVersions(UUID resumeId, UUID userId) {
        List<OptimizationVersion> versions = optimizationVersionRepository.findByResumeIdAndUserId(resumeId, userId);

        return versions.stream()
            .map(this::convertToResponse)
            .toList();
    }

    public OptimizationResponse getOptimizationVersion(UUID versionId, UUID userId) {
        OptimizationVersion version = optimizationVersionRepository.findById(versionId)
            .orElseThrow(() -> ResourceNotFoundException.forEntity("OptimizationVersion", versionId.toString()));

        if (!version.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized access to optimization version");
        }

        return convertToResponse(version);
    }

    public OptimizationResponse activateVersion(UUID versionId, UUID userId) {
        OptimizationVersion version = optimizationVersionRepository.findById(versionId)
            .orElseThrow(() -> ResourceNotFoundException.forEntity("OptimizationVersion", versionId.toString()));

        if (!version.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        // Deactivate other versions for the same resume
        List<OptimizationVersion> otherVersions = optimizationVersionRepository.findByResumeId(version.getResumeId());
        otherVersions.forEach(v -> v.setIsActive(false));
        optimizationVersionRepository.saveAll(otherVersions);

        // Activate this version
        version.setIsActive(true);
        version = optimizationVersionRepository.save(version);
        log.info("Optimization version activated: {} for resume: {}", versionId, version.getResumeId());

        return convertToResponse(version);
    }

    public OptimizationResponse getActiveVersion(UUID resumeId, UUID userId) {
        // Verify ownership
        resumeService.getResume(resumeId, userId);

        OptimizationVersion version = optimizationVersionRepository.findActiveByResumeId(resumeId)
            .orElseThrow(() -> new ResourceNotFoundException("No active optimization version found for resume"));

        return convertToResponse(version);
    }

    private OptimizationResponse convertToResponse(OptimizationVersion version) {
        return OptimizationResponse.builder()
            .versionId(version.getId())
            .resumeId(version.getResumeId())
            .versionNumber(version.getVersionNumber())
            .optimizedContent(version.getOptimizedContent())
            .improvementFocus(version.getImprovementFocus())
            .improvementScore(version.getImprovementScore())
            .changelog(deserializeChangeLog(version.getChangelog()))
            .isActive(version.getIsActive())
            .createdAt(version.getCreatedAt())
            .build();
    }

    private String serializeChangeLog(List<ChangeLog> changeLogs) {
        try {
            return objectMapper.writeValueAsString(changeLogs);
        } catch (Exception e) {
            log.error("Error serializing changelog", e);
            return "[]";
        }
    }

    private List<ChangeLog> deserializeChangeLog(String json) {
        try {
            if (json == null || json.isEmpty()) return new ArrayList<>();
            return objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ChangeLog.class));
        } catch (Exception e) {
            log.error("Error deserializing changelog", e);
            return new ArrayList<>();
        }
    }
}
