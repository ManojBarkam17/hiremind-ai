package com.hiremind.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ResumeRewriterService {

    private static final Map<String, String> WEAK_VERB_REPLACEMENTS = new HashMap<>();
    private static final Map<String, String> WEAK_PHRASE_REPLACEMENTS = new HashMap<>();

    static {
        // Replace weak verbs with stronger action verbs
        WEAK_VERB_REPLACEMENTS.put("worked on", "developed");
        WEAK_VERB_REPLACEMENTS.put("was responsible for", "managed");
        WEAK_VERB_REPLACEMENTS.put("helped", "facilitated");
        WEAK_VERB_REPLACEMENTS.put("did", "delivered");
        WEAK_VERB_REPLACEMENTS.put("made", "engineered");
        WEAK_VERB_REPLACEMENTS.put("used", "leveraged");
        WEAK_VERB_REPLACEMENTS.put("tried", "implemented");
        WEAK_VERB_REPLACEMENTS.put("was involved in", "spearheaded");

        // Replace weak phrases
        WEAK_PHRASE_REPLACEMENTS.put("a lot of", "numerous");
        WEAK_PHRASE_REPLACEMENTS.put("very good", "excellent");
        WEAK_PHRASE_REPLACEMENTS.put("very bad", "critical");
        WEAK_PHRASE_REPLACEMENTS.put("helped to", "enabled");
        WEAK_PHRASE_REPLACEMENTS.put("played a key role in", "led");
    }

    public Map<String, Object> suggestImprovements(String resumeText) {
        Map<String, Object> suggestions = new HashMap<>();

        suggestions.put("weak_verb_suggestions", suggestStrongerVerbs(resumeText));
        suggestions.put("phrase_improvements", suggestPhrasesImprovements(resumeText));
        suggestions.put("quantifiable_metrics", suggestQuantifiableMetrics(resumeText));
        suggestions.put("keyword_additions", suggestMissingKeywords(resumeText));
        suggestions.put("improved_version", generateImprovedVersion(resumeText));

        return suggestions;
    }

    private List<Map<String, String>> suggestStrongerVerbs(String resumeText) {
        List<Map<String, String>> suggestions = new ArrayList<>();

        for (Map.Entry<String, String> entry : WEAK_VERB_REPLACEMENTS.entrySet()) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(entry.getKey()) + "\\b",
                Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(resumeText).find()) {
                Map<String, String> suggestion = new HashMap<>();
                suggestion.put("weak", entry.getKey());
                suggestion.put("replacement", entry.getValue());
                suggestion.put("impact", "Improves action strength and clarity");
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    private List<Map<String, String>> suggestPhrasesImprovements(String resumeText) {
        List<Map<String, String>> suggestions = new ArrayList<>();

        for (Map.Entry<String, String> entry : WEAK_PHRASE_REPLACEMENTS.entrySet()) {
            Pattern pattern = Pattern.compile(Pattern.quote(entry.getKey()),
                Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(resumeText).find()) {
                Map<String, String> suggestion = new HashMap<>();
                suggestion.put("weak_phrase", entry.getKey());
                suggestion.put("replacement", entry.getValue());
                suggestion.put("impact", "More professional and concise");
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    private List<String> suggestQuantifiableMetrics(String resumeText) {
        List<String> suggestions = new ArrayList<>();

        // Check for bullet points without metrics
        String[] lines = resumeText.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("-") || line.trim().startsWith("•")) {
                if (!hasMetrics(line)) {
                    suggestions.add(line.trim() + " - Consider adding metrics (e.g., improved by X%, saved $Y, completed in Z weeks)");
                }
            }
        }

        // Suggest specific metrics for common achievements
        if (resumeText.toLowerCase().contains("improved")) {
            suggestions.add("For 'improved' achievements, specify the percentage or magnitude of improvement");
        }
        if (resumeText.toLowerCase().contains("increased")) {
            suggestions.add("For 'increased' achievements, add the specific percentage or amount increased");
        }
        if (resumeText.toLowerCase().contains("reduced")) {
            suggestions.add("For 'reduced' achievements, include the percentage or amount reduced and impact");
        }

        return suggestions;
    }

    private List<String> suggestMissingKeywords(String resumeText) {
        List<String> suggestions = new ArrayList<>();
        String lowerText = resumeText.toLowerCase();

        // Common technical skills that improve ATS matching
        String[] importantKeywords = {
            "agile", "scrum", "git", "ci/cd", "rest api", "microservices", "cloud",
            "aws", "azure", "docker", "kubernetes", "sql", "nosql", "data structures"
        };

        for (String keyword : importantKeywords) {
            if (!lowerText.contains(keyword)) {
                suggestions.add("Consider adding '" + keyword + "' if relevant to your experience");
            }
        }

        return suggestions;
    }

    private String generateImprovedVersion(String resumeText) {
        String improved = resumeText;

        // Apply all replacements
        for (Map.Entry<String, String> entry : WEAK_VERB_REPLACEMENTS.entrySet()) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(entry.getKey()) + "\\b",
                Pattern.CASE_INSENSITIVE);
            improved = pattern.matcher(improved).replaceAll(entry.getValue());
        }

        for (Map.Entry<String, String> entry : WEAK_PHRASE_REPLACEMENTS.entrySet()) {
            Pattern pattern = Pattern.compile(Pattern.quote(entry.getKey()),
                Pattern.CASE_INSENSITIVE);
            improved = pattern.matcher(improved).replaceAll(entry.getValue());
        }

        // Clean up formatting
        improved = improved.replaceAll("\\s+", " ");
        improved = improved.trim();

        return improved;
    }

    private boolean hasMetrics(String text) {
        return Pattern.compile("\\d+%|\\$\\d+|\\d+\\s*(week|month|year|project|team|user|customer)")
            .matcher(text).find();
    }

    public String optimizeForKeywords(String resumeText, List<String> targetKeywords) {
        String optimized = resumeText;

        for (String keyword : targetKeywords) {
            String lowerText = optimized.toLowerCase();
            String lowerKeyword = keyword.toLowerCase();

            if (!lowerText.contains(lowerKeyword)) {
                // Add keyword naturally to skills section or create one
                if (optimized.toLowerCase().contains("skills")) {
                    optimized = optimized.replaceFirst(
                        "(?i)(skills[:\\s]*)([^\\n]*)",
                        "$1$2, " + keyword
                    );
                } else {
                    optimized += "\n\nSkills: " + String.join(", ", targetKeywords);
                }
            }
        }

        return optimized;
    }
}
