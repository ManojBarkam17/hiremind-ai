package com.hiremind.optimization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationRequest {

    @NotNull(message = "Resume ID is required")
    private UUID resumeId;

    private String improvementFocus; // ATS_OPTIMIZATION, KEYWORD_MATCHING, FORMATTING, COMPREHENSIVE

    private List<String> targetKeywords;

    private String jobDescription; // Optional - for keyword optimization
}
