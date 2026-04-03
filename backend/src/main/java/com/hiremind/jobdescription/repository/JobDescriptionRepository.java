package com.hiremind.jobdescription.repository;

import com.hiremind.jobdescription.entity.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, UUID> {
    List<JobDescription> findByRecruiterId(UUID recruiterId);

    @Query("SELECT jd FROM JobDescription jd WHERE jd.recruiterId = :recruiterId AND jd.isActive = true")
    List<JobDescription> findActiveByRecruiterId(@Param("recruiterId") UUID recruiterId);

    Optional<JobDescription> findByIdAndRecruiterId(UUID id, UUID recruiterId);

    List<JobDescription> findByRecruiterIdOrderByCreatedAtDesc(UUID recruiterId);
}
