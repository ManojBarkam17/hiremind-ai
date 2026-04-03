package com.hiremind.optimization.repository;

import com.hiremind.optimization.entity.OptimizationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptimizationVersionRepository extends JpaRepository<OptimizationVersion, UUID> {
    List<OptimizationVersion> findByResumeId(UUID resumeId);
    List<OptimizationVersion> findByResumeIdOrderByVersionNumberDesc(UUID resumeId);

    @Query("SELECT ov FROM OptimizationVersion ov WHERE ov.resumeId = :resumeId AND ov.isActive = true")
    Optional<OptimizationVersion> findActiveByResumeId(@Param("resumeId") UUID resumeId);

    @Query("SELECT ov FROM OptimizationVersion ov WHERE ov.resumeId = :resumeId AND ov.userId = :userId")
    List<OptimizationVersion> findByResumeIdAndUserId(@Param("resumeId") UUID resumeId, @Param("userId") UUID userId);
}
