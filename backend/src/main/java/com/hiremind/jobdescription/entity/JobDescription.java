package com.hiremind.jobdescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_descriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDescription {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "recruiter_id", nullable = false, columnDefinition = "UUID")
    private UUID recruiterId;

    @Column(nullable = false, length = 255)
    private String jobTitle;

    @Column(nullable = false, length = 500)
    private String companyName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String preferences;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(length = 255)
    private String location;

    @Column(name = "employment_type")
    private String employmentType; // FULL_TIME, PART_TIME, CONTRACT, etc.

    @Column(name = "salary_range")
    private String salaryRange;

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
        this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
