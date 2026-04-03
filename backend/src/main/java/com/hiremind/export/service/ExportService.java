package com.hiremind.export.service;

import com.hiremind.resume.entity.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExportService {

    public String exportAsPlainText(Resume resume) {
        StringBuilder output = new StringBuilder();

        output.append("RESUME - ").append(resume.getFileName()).append("\n");
        output.append("Generated: ").append(resume.getUpdatedAt()).append("\n");
        output.append("=".repeat(80)).append("\n\n");
        output.append(resume.getRawText());

        return output.toString();
    }

    public String exportAsASTOptimized(Resume resume, String jobDescription) {
        StringBuilder output = new StringBuilder();

        output.append("ATS-OPTIMIZED RESUME\n");
        output.append("=".repeat(80)).append("\n\n");

        // Remove complex formatting
        String cleanedText = resume.getRawText()
            .replaceAll("[^\\w\\s.,/-]", "")
            .replaceAll("\\s+", " ");

        output.append(cleanedText);

        return output.toString();
    }

    public String exportAsJSON(Resume resume, Map<String, Object> analysisData) {
        Map<String, Object> exportData = new HashMap<>();

        exportData.put("resume_id", resume.getId().toString());
        exportData.put("file_name", resume.getFileName());
        exportData.put("file_type", resume.getFileType());
        exportData.put("created_at", resume.getCreatedAt().toString());
        exportData.put("updated_at", resume.getUpdatedAt().toString());
        exportData.put("content", resume.getRawText());

        if (analysisData != null) {
            exportData.put("analysis", analysisData);
        }

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(exportData);
        } catch (Exception e) {
            log.error("Error exporting as JSON", e);
            return "{}";
        }
    }

    public String exportAsMarkdown(Resume resume, Map<String, Object> analysisData) {
        StringBuilder output = new StringBuilder();

        output.append("# Resume: ").append(resume.getFileName()).append("\n\n");
        output.append("**Created:** ").append(resume.getCreatedAt()).append("\n");
        output.append("**Last Updated:** ").append(resume.getUpdatedAt()).append("\n\n");

        output.append("## Content\n\n");
        output.append(resume.getRawText()).append("\n\n");

        if (analysisData != null && !analysisData.isEmpty()) {
            output.append("## Analysis Results\n\n");

            if (analysisData.containsKey("score_breakdown")) {
                output.append("### Score Breakdown\n\n");
                Map<String, Object> scoreBreakdown = (Map<String, Object>) analysisData.get("score_breakdown");
                for (Map.Entry<String, Object> entry : scoreBreakdown.entrySet()) {
                    output.append("- **").append(entry.getKey()).append(":** ").append(entry.getValue()).append("\n");
                }
                output.append("\n");
            }

            if (analysisData.containsKey("strengths")) {
                output.append("### Strengths\n\n");
                List<?> strengths = (List<?>) analysisData.get("strengths");
                for (Object strength : strengths) {
                    output.append("- ").append(strength).append("\n");
                }
                output.append("\n");
            }

            if (analysisData.containsKey("weaknesses")) {
                output.append("### Areas for Improvement\n\n");
                List<?> weaknesses = (List<?>) analysisData.get("weaknesses");
                for (Object weakness : weaknesses) {
                    output.append("- ").append(weakness).append("\n");
                }
                output.append("\n");
            }
        }

        return output.toString();
    }

    public String generateATSFriendlyVersion(Resume resume) {
        StringBuilder output = new StringBuilder();

        // Use plain text, standard fonts
        String cleaned = resume.getRawText()
            // Remove special characters except basic punctuation
            .replaceAll("[^\\w\\s.,()/-]", "")
            // Normalize whitespace
            .replaceAll("\\s+", " ")
            .trim();

        // Ensure proper structure
        if (!cleaned.toLowerCase().contains("experience")) {
            output.append("EXPERIENCE\n");
        }
        if (!cleaned.toLowerCase().contains("education")) {
            output.append("\nEDUCATION\n");
        }
        if (!cleaned.toLowerCase().contains("skills")) {
            output.append("\nSKILLS\n");
        }

        output.append(cleaned);

        return output.toString();
    }

    public byte[] generatePDFExport(Resume resume, String format) {
        // This is a placeholder for PDF generation
        // In a production system, use iText or Apache PDFBox
        String content = switch (format) {
            case "ATS_OPTIMIZED" -> exportAsASTOptimized(resume, "");
            case "MARKDOWN" -> exportAsMarkdown(resume, null);
            case "PLAIN_TEXT" -> exportAsPlainText(resume);
            default -> exportAsPlainText(resume);
        };

        return content.getBytes();
    }
}
