package com.hiremind.optimization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationResponse {

    @JsonProperty("version_id")
    private UUID versionId;

    @JsonProperty("resume_id")
    private UUID resumeId;

    @JsonProperty("version_number")
    private Integer versionNumber;

    @JsonProperty("optimized_content")
    private String optimizedContent;

    @JsonProperty("improvement_focus")
    private String improvementFocus;

    @JsonProperty("improvement_score")
    private Double improvementScore;

    @JsonProperty("changelog")
    private List<ChangeLog> changelog;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("message")
    private String message;
}
