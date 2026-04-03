package com.hiremind.analysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    @NotNull(message = "Resume ID is required")
    private UUID resumeId;

    private UUID jobDescriptionId;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    private String analysisType; // RESUME_JD_MATCH, ATS_ANALYSIS, COMPREHENSIVE
}
