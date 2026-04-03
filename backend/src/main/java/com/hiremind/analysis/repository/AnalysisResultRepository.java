package com.hiremind.analysis.repository;

import com.hiremind.analysis.entity.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID> {
    List<AnalysisResult> findByUserId(UUID userId);
    List<AnalysisResult> findByResumeId(UUID resumeId);
    List<AnalysisResult> findByJobDescriptionId(UUID jobDescriptionId);

    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.resumeId = :resumeId AND ar.userId = :userId ORDER BY ar.createdAt DESC")
    List<AnalysisResult> findByResumeIdAndUserId(@Param("resumeId") UUID resumeId, @Param("userId") UUID userId);

    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.resumeId = :resumeId AND ar.jobDescriptionId = :jdId")
    Optional<AnalysisResult> findByResumeAndJobDescription(@Param("resumeId") UUID resumeId, @Param("jdId") UUID jdId);

    List<AnalysisResult> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
