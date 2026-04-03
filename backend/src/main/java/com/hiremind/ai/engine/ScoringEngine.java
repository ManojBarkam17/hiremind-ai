package com.hiremind.ai.engine;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.*;
import java.util.*;

@Component
@Slf4j
public class ScoringEngine {

    // Weighted scoring constants - must sum to 100
    private static final double REQUIRED_SKILLS_WEIGHT = 0.35;
    private static final double PREFERRED_SKILLS_WEIGHT = 0.15;
    private static final double SEMANTIC_SIMILARITY_WEIGHT = 0.15;
    private static final double DOMAIN_ALIGNMENT_WEIGHT = 0.10;
    private static final double ATS_KEYWORDS_WEIGHT = 0.15;
    private static final double FORMATTING_WEIGHT = 0.10;

    @Data
    @Builder
    public static class ScoreBreakdown {
        private double requiredSkillsScore;
        private double preferredSkillsScore;
        private double semanticSimilarityScore;
        private double domainAlignmentScore;
        private double atsKeywordsScore;
        private double formattingScore;
        private double totalScore;
        private String matchLevel; // HIGH, MEDIUM, LOW
        private List<String> strengths;
        private List<String> weaknesses;
        private List<String> recommendations;
    }

    public ScoreBreakdown calculateScore(
        String resumeText,
        List<String> resumeSkills,
        String jobTitle,
        List<String> requiredSkills,
        List<String> preferredSkills,
        String jobDescription
    ) {
        double requiredScore = calculateRequiredSkillsScore(resumeSkills, requiredSkills);
        double preferredScore = calculatePreferredSkillsScore(resumeSkills, preferredSkills);
        double semanticScore = calculateSemanticSimilarity(resumeText, jobDescription);
        double domainScore = calculateDomainAlignment(resumeText, jobTitle);
        double atsScore = calculateATSKeywordsScore(resumeText, requiredSkills, preferredSkills);
        double formattingScore = calculateFormattingScore(resumeText);

        double totalScore = (requiredScore * REQUIRED_SKILLS_WEIGHT) +
                           (preferredScore * PREFERRED_SKILLS_WEIGHT) +
                           (semanticScore * SEMANTIC_SIMILARITY_WEIGHT) +
                           (domainScore * DOMAIN_ALIGNMENT_WEIGHT) +
                           (atsScore * ATS_KEYWORDS_WEIGHT) +
                           (formattingScore * FORMATTING_WEIGHT);

        String matchLevel = determineMatchLevel(totalScore);
        List<String> strengths = identifyStrengths(resumeSkills, requiredSkills, preferredSkills, totalScore);
        List<String> weaknesses = identifyWeaknesses(resumeSkills, requiredSkills, preferredSkills);
        List<String> recommendations = generateRecommendations(weaknesses, requiredSkills, resumeSkills);

        return ScoreBreakdown.builder()
            .requiredSkillsScore(requiredScore)
            .preferredSkillsScore(preferredScore)
            .semanticSimilarityScore(semanticScore)
            .domainAlignmentScore(domainScore)
            .atsKeywordsScore(atsScore)
            .formattingScore(formattingScore)
            .totalScore(Math.round(totalScore * 100.0) / 100.0)
            .matchLevel(matchLevel)
            .strengths(strengths)
            .weaknesses(weaknesses)
            .recommendations(recommendations)
            .build();
    }

    private double calculateRequiredSkillsScore(List<String> resumeSkills, List<String> requiredSkills) {
        if (requiredSkills.isEmpty()) {
            return 50.0; // Neutral if no required skills specified
        }

        long matchCount = requiredSkills.stream()
            .filter(skill -> hasSkillMatch(resumeSkills, skill))
            .count();

        return (matchCount / (double) requiredSkills.size()) * 100.0;
    }

    private double calculatePreferredSkillsScore(List<String> resumeSkills, List<String> preferredSkills) {
        if (preferredSkills.isEmpty()) {
            return 50.0;
        }

        long matchCount = preferredSkills.stream()
            .filter(skill -> hasSkillMatch(resumeSkills, skill))
            .count();

        return (matchCount / (double) preferredSkills.size()) * 100.0;
    }

    private double calculateSemanticSimilarity(String resumeText, String jobDescription) {
        // Simple semantic similarity based on common keywords
        Set<String> resumeKeywords = extractKeywords(resumeText);
        Set<String> jobKeywords = extractKeywords(jobDescription);

        if (jobKeywords.isEmpty()) {
            return 50.0;
        }

        Set<String> intersection = new HashSet<>(resumeKeywords);
        intersection.retainAll(jobKeywords);

        return (intersection.size() / (double) jobKeywords.size()) * 100.0;
    }

    private double calculateDomainAlignment(String resumeText, String jobTitle) {
        String lowerResume = resumeText.toLowerCase();
        String lowerJobTitle = jobTitle.toLowerCase();

        Map<String, Set<String>> domainKeywords = getDomainKeywords();

        // Find which domain the job title belongs to
        String jobDomain = extractDomainFromJobTitle(jobTitle);

        if (jobDomain.isEmpty()) {
            return 50.0;
        }

        Set<String> domainSkills = domainKeywords.getOrDefault(jobDomain, new HashSet<>());
        long matches = domainSkills.stream()
            .filter(skill -> lowerResume.contains(skill.toLowerCase()))
            .count();

        return (matches / (double) Math.max(1, domainSkills.size())) * 100.0;
    }

    private double calculateATSKeywordsScore(String resumeText, List<String> requiredSkills, List<String> preferredSkills) {
        String lowerResume = resumeText.toLowerCase();
        List<String> allSkills = new ArrayList<>(requiredSkills);
        allSkills.addAll(preferredSkills);

        long foundSkills = allSkills.stream()
            .filter(skill -> lowerResume.contains(skill.toLowerCase()))
            .count();

        return allSkills.isEmpty() ? 50.0 : (foundSkills / (double) allSkills.size()) * 100.0;
    }

    private double calculateFormattingScore(String resumeText) {
        double score = 100.0;

        if (resumeText.contains("<")) score -= 10; // HTML tags
        if (resumeText.contains("^") || resumeText.contains("✓")) score -= 5; // Special chars
        if (resumeText.length() < 300) score -= 15; // Too short
        if (resumeText.length() > 10000) score -= 5; // Too long
        if (!hasProperSections(resumeText)) score -= 10; // Missing sections

        return Math.max(0, Math.min(100, score));
    }

    private boolean hasSkillMatch(List<String> resumeSkills, String requiredSkill) {
        return resumeSkills.stream()
            .anyMatch(skill -> skill.toLowerCase().contains(requiredSkill.toLowerCase()) ||
                              requiredSkill.toLowerCase().contains(skill.toLowerCase()));
    }

    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new HashSet<>();
        String[] words = text.toLowerCase()
            .replaceAll("[^a-z0-9\\s+#.-]", " ")
            .split("\\s+");

        for (String word : words) {
            if (word.length() > 3) {
                keywords.add(word);
            }
        }

        return keywords;
    }

    private Map<String, Set<String>> getDomainKeywords() {
        Map<String, Set<String>> domains = new HashMap<>();

        domains.put("backend", new HashSet<>(Arrays.asList(
            "java", "python", "spring", "flask", "django", "api", "rest", "microservices"
        )));

        domains.put("frontend", new HashSet<>(Arrays.asList(
            "javascript", "react", "angular", "vue", "css", "html", "ui", "ux"
        )));

        domains.put("devops", new HashSet<>(Arrays.asList(
            "docker", "kubernetes", "ci/cd", "jenkins", "terraform", "ansible", "aws", "azure"
        )));

        domains.put("data", new HashSet<>(Arrays.asList(
            "python", "sql", "spark", "hadoop", "etl", "analytics", "tableau", "powerbi"
        )));

        return domains;
    }

    private String extractDomainFromJobTitle(String jobTitle) {
        String lowerTitle = jobTitle.toLowerCase();

        if (lowerTitle.contains("backend") || lowerTitle.contains("java") || lowerTitle.contains("python")) {
            return "backend";
        } else if (lowerTitle.contains("frontend") || lowerTitle.contains("react") || lowerTitle.contains("javascript")) {
            return "frontend";
        } else if (lowerTitle.contains("devops") || lowerTitle.contains("infrastructure")) {
            return "devops";
        } else if (lowerTitle.contains("data") || lowerTitle.contains("analytics")) {
            return "data";
        }

        return "";
    }

    private String determineMatchLevel(double score) {
        if (score >= 75) {
            return "HIGH";
        } else if (score >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    private List<String> identifyStrengths(
        List<String> resumeSkills,
        List<String> requiredSkills,
        List<String> preferredSkills,
        double totalScore
    ) {
        List<String> strengths = new ArrayList<>();

        long requiredMatches = requiredSkills.stream()
            .filter(skill -> hasSkillMatch(resumeSkills, skill))
            .count();

        if (requiredMatches >= requiredSkills.size() * 0.8) {
            strengths.add("Strong match with required skills (" + requiredMatches + "/" + requiredSkills.size() + ")");
        }

        long preferredMatches = preferredSkills.stream()
            .filter(skill -> hasSkillMatch(resumeSkills, skill))
            .count();

        if (preferredMatches >= preferredSkills.size() * 0.5) {
            strengths.add("Good coverage of preferred skills (" + preferredMatches + "/" + preferredSkills.size() + ")");
        }

        if (totalScore >= 75) {
            strengths.add("Overall excellent match with job requirements");
        }

        if (strengths.isEmpty()) {
            strengths.add("Resume presents relevant professional experience");
        }

        return strengths;
    }

    private List<String> identifyWeaknesses(
        List<String> resumeSkills,
        List<String> requiredSkills,
        List<String> preferredSkills
    ) {
        List<String> weaknesses = new ArrayList<>();

        List<String> missingRequired = requiredSkills.stream()
            .filter(skill -> !hasSkillMatch(resumeSkills, skill))
            .toList();

        if (!missingRequired.isEmpty()) {
            weaknesses.add("Missing " + missingRequired.size() + " required skills: " +
                          String.join(", ", missingRequired.stream().limit(3).toList()));
        }

        List<String> missingPreferred = preferredSkills.stream()
            .filter(skill -> !hasSkillMatch(resumeSkills, skill))
            .toList();

        if (!missingPreferred.isEmpty() && missingPreferred.size() > preferredSkills.size() * 0.5) {
            weaknesses.add("Limited preferred skills coverage");
        }

        if (weaknesses.isEmpty()) {
            weaknesses.add("No significant gaps identified");
        }

        return weaknesses;
    }

    private List<String> generateRecommendations(
        List<String> weaknesses,
        List<String> requiredSkills,
        List<String> resumeSkills
    ) {
        List<String> recommendations = new ArrayList<>();

        // Find missing required skills
        List<String> missingRequired = requiredSkills.stream()
            .filter(skill -> !hasSkillMatch(resumeSkills, skill))
            .limit(3)
            .toList();

        if (!missingRequired.isEmpty()) {
            recommendations.add("Highlight any experience with: " + String.join(", ", missingRequired));
        }

        recommendations.add("Quantify achievements with metrics (percentages, numbers, impact)");
        recommendations.add("Ensure skills section is comprehensive and includes technical keywords");
        recommendations.add("Tailor resume language to match job description terminology");
        recommendations.add("Include specific project examples demonstrating required skills");

        return recommendations;
    }

    private boolean hasProperSections(String resumeText) {
        String lowerText = resumeText.toLowerCase();
        return lowerText.contains("experience") && lowerText.contains("education");
    }
}
