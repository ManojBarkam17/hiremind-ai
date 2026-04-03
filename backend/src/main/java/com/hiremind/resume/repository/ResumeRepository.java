package com.hiremind.resume.repository;

import com.hiremind.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByUserId(UUID userId);
    Optional<Resume> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT r FROM Resume r WHERE r.userId = :userId AND r.isPrimary = true")
    Optional<Resume> findPrimaryResumeByUserId(@Param("userId") UUID userId);

    List<Resume> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
