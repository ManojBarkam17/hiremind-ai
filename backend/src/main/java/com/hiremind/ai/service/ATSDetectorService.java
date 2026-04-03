package com.hiremind.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.*;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ATSDetectorService {

    public Map<String, Object> analyzeATSCompatibility(String resumeText) {
        Map<String, Object> analysis = new HashMap<>();

        analysis.put("ats_score", calculateATSScore(resumeText));
        analysis.put("formatting_issues", detectFormattingIssues(resumeText));
        analysis.put("keyword_density", analyzeKeywordDensity(resumeText));
        analysis.put("readability_score", calculateReadabilityScore(resumeText));
        analysis.put("recommendations", generateATSRecommendations(resumeText));

        return analysis;
    }

    private double calculateATSScore(String resumeText) {
        double score = 100.0;

        // Penalize for problematic elements
        if (containsImages(resumeText)) score -= 10;
        if (containsTables(resumeText)) score -= 5;
        if (hasComplexFormatting(resumeText)) score -= 5;
        if (hasURLs(resumeText)) score -= 2;
        if (hasSpecialCharacters(resumeText)) score -= 3;

        // Reward for good practices
        if (hasKeySection(resumeText, "Skills")) score += 5;
        if (hasKeySection(resumeText, "Experience")) score += 5;
        if (hasKeySection(resumeText, "Education")) score += 5;
        if (isProperLength(resumeText)) score += 3;

        return Math.max(0, Math.min(100, score));
    }

    private List<String> detectFormattingIssues(String resumeText) {
        List<String> issues = new ArrayList<>();

        if (containsImages(resumeText)) {
            issues.add("Resume contains images which may not be parsed by ATS");
        }
        if (containsTables(resumeText)) {
            issues.add("Resume uses tables which can cause parsing issues");
        }
        if (hasComplexFormatting(resumeText)) {
            issues.add("Complex formatting detected - use simple text formatting");
        }
        if (hasMultipleColumns(resumeText)) {
            issues.add("Multi-column layout detected - use single column format");
        }
        if (hasUnusualFonts(resumeText)) {
            issues.add("Unusual fonts detected - use standard fonts (Arial, Calibri, Times New Roman)");
        }
        if (hasPageBreaks(resumeText)) {
            issues.add("Page breaks detected - may disrupt ATS parsing");
        }

        return issues;
    }

    private Map<String, Double> analyzeKeywordDensity(String resumeText) {
        Map<String, Double> keywordDensity = new HashMap<>();

        String[] techKeywords = {
            "java", "python", "javascript", "spring", "react", "sql", "aws", "docker",
            "kubernetes", "git", "agile", "scrum", "api", "rest", "microservices"
        };

        int totalWords = resumeText.split("\\s+").length;

        for (String keyword : techKeywords) {
            int count = countOccurrences(resumeText.toLowerCase(), keyword);
            double density = (count / (double) Math.max(1, totalWords)) * 100;
            if (density > 0) {
                keywordDensity.put(keyword, density);
            }
        }

        return keywordDensity;
    }

    private double calculateReadabilityScore(String resumeText) {
        double score = 0.0;

        // Analyze sentence length
        String[] sentences = resumeText.split("[.!?]+");
        double avgSentenceLength = (double) resumeText.split("\\s+").length / Math.max(1, sentences.length);

        if (avgSentenceLength < 25) score += 20; // Good
        else if (avgSentenceLength < 35) score += 15; // Acceptable
        else score += 5; // Too long

        // Analyze word length
        double avgWordLength = (double) resumeText.replaceAll("\\s+", "").length() /
                              Math.max(1, resumeText.split("\\s+").length);

        if (avgWordLength < 5.5) score += 20;
        else if (avgWordLength < 6.5) score += 15;
        else score += 5;

        // Paragraph structure
        int paragraphs = resumeText.split("\n\n+").length;
        if (paragraphs >= 3 && paragraphs <= 8) score += 20;
        else score += 10;

        // Action verbs
        if (hasActionVerbs(resumeText)) score += 10;

        return Math.min(100, score);
    }

    private List<String> generateATSRecommendations(String resumeText) {
        List<String> recommendations = new ArrayList<>();

        if (!hasKeySection(resumeText, "Skills")) {
            recommendations.add("Add a dedicated Skills section listing relevant technologies");
        }

        if (!hasKeySection(resumeText, "Experience")) {
            recommendations.add("Ensure you have clear work experience section with company names and dates");
        }

        if (!hasKeySection(resumeText, "Education")) {
            recommendations.add("Include Education section with degree and institution");
        }

        if (!hasActionVerbs(resumeText)) {
            recommendations.add("Use action verbs (Developed, Implemented, Designed, etc.) to describe accomplishments");
        }

        if (containsTables(resumeText)) {
            recommendations.add("Convert tables to simple bullet points format");
        }

        if (resumeText.length() < 500) {
            recommendations.add("Resume appears too short - add more details about experiences and accomplishments");
        }

        if (resumeText.length() > 5000) {
            recommendations.add("Resume appears too long - aim for 1-2 pages. Remove redundant information");
        }

        if (hasURLs(resumeText)) {
            recommendations.add("URLs may not parse correctly in ATS - consider using text format like 'linkedin.com/in/yourprofile'");
        }

        if (!hasDateFormat(resumeText)) {
            recommendations.add("Include clear date ranges (MM/YYYY format) for all experiences");
        }

        return recommendations;
    }

    // Helper methods
    private boolean containsImages(String text) {
        return text.contains("[image]") || text.contains("<img") || text.contains("PNG") || text.contains("JPG");
    }

    private boolean containsTables(String text) {
        return text.contains("table") || text.contains("|");
    }

    private boolean hasComplexFormatting(String text) {
        return text.contains("^") || text.contains("✓") || text.contains("★");
    }

    private boolean hasMultipleColumns(String text) {
        return text.contains("column");
    }

    private boolean hasUnusualFonts(String text) {
        // Detect unusual font references
        return text.contains("wingdings") || text.contains("symbol");
    }

    private boolean hasPageBreaks(String text) {
        return text.contains("pagebreak") || text.contains("---");
    }

    private boolean hasKeySection(String text, String sectionName) {
        return Pattern.compile("\\b" + sectionName + "\\b", Pattern.CASE_INSENSITIVE)
            .matcher(text).find();
    }

    private int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

    private boolean hasActionVerbs(String text) {
        String[] verbs = {"developed", "implemented", "designed", "created", "built", "managed",
                         "led", "directed", "coordinated", "improved", "optimized", "delivered"};
        String lowerText = text.toLowerCase();
        for (String verb : verbs) {
            if (lowerText.contains(verb)) return true;
        }
        return false;
    }

    private boolean isProperLength(String text) {
        int wordCount = text.split("\\s+").length;
        return wordCount >= 300 && wordCount <= 1000;
    }

    private boolean hasURLs(String text) {
        return Pattern.compile("https?://").matcher(text).find();
    }

    private boolean hasDateFormat(String text) {
        return Pattern.compile("\\d{1,2}/\\d{4}|\\d{4}").matcher(text).find();
    }
}
