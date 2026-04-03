package com.hiremind.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {

    @JsonProperty("analysis_id")
    private UUID analysisId;

    @JsonProperty("resume_id")
    private UUID resumeId;

    @JsonProperty("job_description_id")
    private UUID jobDescriptionId;

    @JsonProperty("score_breakdown")
    private ScoreBreakdown scoreBreakdown;

    @JsonProperty("ats_analysis")
    private Map<String, Object> atsAnalysis;

    @JsonProperty("extracted_skills")
    private Map<String, Object> extractedSkills;

    @JsonProperty("job_analysis")
    private Map<String, Object> jobAnalysis;

    @JsonProperty("optimizations")
    private Map<String, Object> optimizations;

    @JsonProperty("analysis_type")
    private String analysisType;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
