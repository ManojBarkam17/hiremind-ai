package com.hiremind.analysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiremind.ai.service.AIService;
import com.hiremind.analysis.dto.AnalysisRequest;
import com.hiremind.analysis.dto.AnalysisResponse;
import com.hiremind.analysis.dto.ScoreBreakdown;
import com.hiremind.analysis.entity.AnalysisResult;
import com.hiremind.analysis.repository.AnalysisResultRepository;
import com.hiremind.common.exception.ResourceNotFoundException;
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
public class AnalysisService {

    private final AnalysisResultRepository analysisResultRepository;
    private final ResumeService resumeService;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    public AnalysisResponse analyzeResume(UUID userId, AnalysisRequest request) {
        // Get resume
        Resume resume = resumeService.getResume(request.getResumeId(), userId);

        String analysisType = request.getAnalysisType() != null ? request.getAnalysisType() : "COMPREHENSIVE";

        Map<String, Object> aiAnalysis;

        if ("COMPREHENSIVE".equalsIgnoreCase(analysisType)) {
            aiAnalysis = aiService.comprehensiveAnalysis(
                resume.getRawText(),
                request.getJobTitle(),
                request.getJobDescription()
            );
        } else if ("ATS_ANALYSIS".equalsIgnoreCase(analysisType)) {
            aiAnalysis = aiService.analyzeATSCompatibility(resume.getRawText());
        } else {
            aiAnalysis = aiService.analyzeResumeAgainstJD(
                resume.getRawText(),
                request.getJobTitle(),
                request.getJobDescription()
            );
        }

        // Extract score breakdown
        ScoreBreakdown scoreBreakdown = null;
        Map<String, Object> resumeJDMatch = (Map<String, Object>) aiAnalysis.get("resume_jd_match");
        if (resumeJDMatch != null) {
            scoreBreakdown = ScoreBreakdown.fromEngine(
                (com.hiremind.ai.engine.ScoringEngine.ScoreBreakdown) resumeJDMatch.get("score_breakdown")
            );
        }

        // Save analysis result
        AnalysisResult analysisResult = AnalysisResult.builder()
            .resumeId(request.getResumeId())
            .jobDescriptionId(request.getJobDescriptionId())
            .userId(userId)
            .analysisType(analysisType)
            .overallScore(scoreBreakdown != null ? scoreBreakdown.getTotalScore() : 0.0)
            .requiredSkillsScore(scoreBreakdown != null ? scoreBreakdown.getRequiredSkillsScore() : 0.0)
            .preferredSkillsScore(scoreBreakdown != null ? scoreBreakdown.getPreferredSkillsScore() : 0.0)
            .semanticSimilarityScore(scoreBreakdown != null ? scoreBreakdown.getSemanticSimilarityScore() : 0.0)
            .domainAlignmentScore(scoreBreakdown != null ? scoreBreakdown.getDomainAlignmentScore() : 0.0)
            .atsKeywordsScore(scoreBreakdown != null ? scoreBreakdown.getAtsKeywordsScore() : 0.0)
            .formattingScore(scoreBreakdown != null ? scoreBreakdown.getFormattingScore() : 0.0)
            .matchLevel(scoreBreakdown != null ? scoreBreakdown.getMatchLevel() : "UNKNOWN")
            .strengths(serializeList(scoreBreakdown != null ? scoreBreakdown.getStrengths() : new ArrayList<>()))
            .weaknesses(serializeList(scoreBreakdown != null ? scoreBreakdown.getWeaknesses() : new ArrayList<>()))
            .recommendations(serializeList(scoreBreakdown != null ? scoreBreakdown.getRecommendations() : new ArrayList<>()))
            .atsAnalysis(serializeMap((Map<String, Object>) aiAnalysis.get("ats_compatibility")))
            .extractedSkills(serializeMap((Map<String, Object>) aiAnalysis.get("extracted_skills")))
            .build();

        analysisResult = analysisResultRepository.save(analysisResult);
        log.info("Analysis saved: {} for resume: {}", analysisResult.getId(), request.getResumeId());

        return AnalysisResponse.builder()
            .analysisId(analysisResult.getId())
            .resumeId(analysisResult.getResumeId())
            .jobDescriptionId(analysisResult.getJobDescriptionId())
            .scoreBreakdown(scoreBreakdown)
            .atsAnalysis((Map<String, Object>) aiAnalysis.get("ats_compatibility"))
            .extractedSkills((Map<String, Object>) aiAnalysis.get("extracted_skills"))
            .jobAnalysis((Map<String, Object>) resumeJDMatch != null ? resumeJDMatch.get("job_analysis") : new HashMap<>())
            .optimizations((Map<String, Object>) aiAnalysis.get("optimizations"))
            .analysisType(analysisType)
            .createdAt(analysisResult.getCreatedAt())
            .build();
    }

    public AnalysisResponse getAnalysis(UUID analysisId, UUID userId) {
        AnalysisResult result = analysisResultRepository.findById(analysisId)
            .orElseThrow(() -> ResourceNotFoundException.forEntity("AnalysisResult", analysisId.toString()));

        if (!result.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized access to analysis");
        }

        return AnalysisResponse.builder()
            .analysisId(result.getId())
            .resumeId(result.getResumeId())
            .jobDescriptionId(result.getJobDescriptionId())
            .scoreBreakdown(ScoreBreakdown.builder()
                .totalScore(result.getOverallScore())
                .requiredSkillsScore(result.getRequiredSkillsScore())
                .preferredSkillsScore(result.getPreferredSkillsScore())
                .semanticSimilarityScore(result.getSemanticSimilarityScore())
                .domainAlignmentScore(result.getDomainAlignmentScore())
                .atsKeywordsScore(result.getAtsKeywordsScore())
                .formattingScore(result.getFormattingScore())
                .matchLevel(result.getMatchLevel())
                .strengths(deserializeList(result.getStrengths()))
                .weaknesses(deserializeList(result.getWeaknesses()))
                .recommendations(deserializeList(result.getRecommendations()))
                .build())
            .atsAnalysis(deserializeMap(result.getAtsAnalysis()))
            .extractedSkills(deserializeMap(result.getExtractedSkills()))
            .analysisType(result.getAnalysisType())
            .createdAt(result.getCreatedAt())
            .build();
    }

    public List<AnalysisResponse> getUserAnalyses(UUID userId) {
        List<AnalysisResult> results = analysisResultRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return results.stream()
            .map(this::convertToResponse)
            .toList();
    }

    public List<AnalysisResponse> getResumeAnalyses(UUID resumeId, UUID userId) {
        List<AnalysisResult> results = analysisResultRepository.findByResumeIdAndUserId(resumeId, userId);

        return results.stream()
            .map(this::convertToResponse)
            .toList();
    }

    private AnalysisResponse convertToResponse(AnalysisResult result) {
        return AnalysisResponse.builder()
            .analysisId(result.getId())
            .resumeId(result.getResumeId())
            .jobDescriptionId(result.getJobDescriptionId())
            .scoreBreakdown(ScoreBreakdown.builder()
                .totalScore(result.getOverallScore())
                .requiredSkillsScore(result.getRequiredSkillsScore())
                .preferredSkillsScore(result.getPreferredSkillsScore())
                .matchLevel(result.getMatchLevel())
                .build())
            .analysisType(result.getAnalysisType())
            .createdAt(result.getCreatedAt())
            .build();
    }

    private String serializeList(List<?> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            log.error("Error serializing list", e);
            return "[]";
        }
    }

    private String serializeMap(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map != null ? map : new HashMap<>());
        } catch (Exception e) {
            log.error("Error serializing map", e);
            return "{}";
        }
    }

    private List<String> deserializeList(String json) {
        try {
            if (json == null || json.isEmpty()) return new ArrayList<>();
            return objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            log.error("Error deserializing list", e);
            return new ArrayList<>();
        }
    }

    private Map<String, Object> deserializeMap(String json) {
        try {
            if (json == null || json.isEmpty()) return new HashMap<>();
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("Error deserializing map", e);
            return new HashMap<>();
        }
    }
}
