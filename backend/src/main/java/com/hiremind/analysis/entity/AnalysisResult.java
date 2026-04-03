package com.hiremind.analysis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analysis_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "resume_id", nullable = false, columnDefinition = "UUID")
    private UUID resumeId;

    @Column(name = "job_description_id", columnDefinition = "UUID")
    private UUID jobDescriptionId;

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @Column(nullable = false)
    private Double overallScore;

    @Column
    private Double requiredSkillsScore;

    @Column
    private Double preferredSkillsScore;

    @Column
    private Double semanticSimilarityScore;

    @Column
    private Double domainAlignmentScore;

    @Column
    private Double atsKeywordsScore;

    @Column
    private Double formattingScore;

    @Column(name = "match_level")
    private String matchLevel; // HIGH, MEDIUM, LOW

    @Lob
    @Column(columnDefinition = "TEXT")
    private String strengths; // JSON array

    @Lob
    @Column(columnDefinition = "TEXT")
    private String weaknesses; // JSON array

    @Lob
    @Column(columnDefinition = "TEXT")
    private String recommendations; // JSON array

    @Lob
    @Column(columnDefinition = "TEXT")
    private String atsAnalysis; // JSON

    @Lob
    @Column(columnDefinition = "TEXT")
    private String extractedSkills; // JSON

    @Column(name = "analysis_type")
    private String analysisType; // RESUME_JD_MATCH, ATS_ANALYSIS, OPTIMIZATION_SUGGESTION

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
