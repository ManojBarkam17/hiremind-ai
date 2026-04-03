package com.hiremind.optimization.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "optimization_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationVersion {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "resume_id", nullable = false, columnDefinition = "UUID")
    private UUID resumeId;

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String optimizedContent;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String changelog; // JSON array

    @Column(name = "improvement_focus")
    private String improvementFocus; // ATS_OPTIMIZATION, KEYWORD_MATCHING, FORMATTING, etc.

    @Column(nullable = false)
    private Double improvementScore;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
