package com.hiremind.ai.service;

import com.hiremind.ai.engine.ScoringEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIService {

    private final SkillExtractorService skillExtractorService;
    private final JDParserService jdParserService;
    private final ATSDetectorService atsDetectorService;
    private final ResumeRewriterService resumeRewriterService;
    private final ScoringEngine scoringEngine;

    public Map<String, Object> analyzeResumeAgainstJD(
        String resumeText,
        String jobTitle,
        String jobDescription
    ) {
        Map<String, Object> analysis = new HashMap<>();

        try {
            // Extract resume skills
            Map<String, Object> resumeSkillsData = skillExtractorService.extractSkills(resumeText);
            List<String> resumeSkills = (List<String>) resumeSkillsData.getOrDefault("all_skills", new ArrayList<>());

            // Parse job description
            Map<String, Object> jdData = jdParserService.parseJobDescription(jobDescription);
            List<String> requiredSkills = (List<String>) jdData.getOrDefault("required_skills", new ArrayList<>());
            List<String> preferredSkills = (List<String>) jdData.getOrDefault("preferred_skills", new ArrayList<>());

            // Calculate match score
            ScoringEngine.ScoreBreakdown scoreBreakdown = scoringEngine.calculateScore(
                resumeText,
                resumeSkills,
                jobTitle,
                requiredSkills,
                preferredSkills,
                jobDescription
            );

            analysis.put("score_breakdown", scoreBreakdown);
            analysis.put("resume_skills", resumeSkills);
            analysis.put("job_analysis", jdData);

        } catch (Exception e) {
            log.error("Error analyzing resume against JD", e);
            analysis.put("error", e.getMessage());
        }

        return analysis;
    }

    public Map<String, Object> analyzeATSCompatibility(String resumeText) {
        Map<String, Object> analysis = new HashMap<>();

        try {
            analysis.put("ats_analysis", atsDetectorService.analyzeATSCompatibility(resumeText));
        } catch (Exception e) {
            log.error("Error analyzing ATS compatibility", e);
            analysis.put("error", e.getMessage());
        }

        return analysis;
    }

    public Map<String, Object> suggestOptimizations(String resumeText) {
        Map<String, Object> analysis = new HashMap<>();

        try {
            Map<String, Object> improvements = resumeRewriterService.suggestImprovements(resumeText);
            analysis.put("improvements", improvements);
            analysis.put("optimization_suggestions", improvements);
        } catch (Exception e) {
            log.error("Error suggesting optimizations", e);
            analysis.put("error", e.getMessage());
        }

        return analysis;
    }

    public Map<String, Object> optimizeResumeForKeywords(String resumeText, List<String> keywords) {
        Map<String, Object> result = new HashMap<>();

        try {
            String optimized = resumeRewriterService.optimizeForKeywords(resumeText, keywords);
            result.put("optimized_resume", optimized);
            result.put("keywords_added", keywords);
        } catch (Exception e) {
            log.error("Error optimizing resume for keywords", e);
            result.put("error", e.getMessage());
        }

        return result;
    }

    public Map<String, Object> comprehensiveAnalysis(
        String resumeText,
        String jobTitle,
        String jobDescription
    ) {
        Map<String, Object> comprehensive = new HashMap<>();

        try {
            // 1. Resume to JD matching
            comprehensive.put("resume_jd_match", analyzeResumeAgainstJD(resumeText, jobTitle, jobDescription));

            // 2. ATS compatibility
            comprehensive.put("ats_compatibility", analyzeATSCompatibility(resumeText));

            // 3. Optimization suggestions
            comprehensive.put("optimizations", suggestOptimizations(resumeText));

            // Extract and include resume skills
            Map<String, Object> skillsData = skillExtractorService.extractSkills(resumeText);
            comprehensive.put("extracted_skills", skillsData);

        } catch (Exception e) {
            log.error("Error in comprehensive analysis", e);
            comprehensive.put("error", e.getMessage());
        }

        return comprehensive;
    }
}
