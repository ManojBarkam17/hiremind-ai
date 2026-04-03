package com.hiremind.jobdescription.service;

import com.hiremind.common.exception.ResourceNotFoundException;
import com.hiremind.jobdescription.dto.JobDescriptionRequest;
import com.hiremind.jobdescription.entity.JobDescription;
import com.hiremind.jobdescription.repository.JobDescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JobDescriptionService {

    private final JobDescriptionRepository jobDescriptionRepository;

    public JobDescription createJobDescription(UUID recruiterId, JobDescriptionRequest request) {
        JobDescription jd = JobDescription.builder()
            .recruiterId(recruiterId)
            .jobTitle(request.getJobTitle())
            .companyName(request.getCompanyName())
            .description(request.getDescription())
            .requirements(request.getRequirements())
            .preferences(request.getPreferences())
            .responsibilities(request.getResponsibilities())
            .location(request.getLocation())
            .employmentType(request.getEmploymentType())
            .salaryRange(request.getSalaryRange())
            .build();

        jd = jobDescriptionRepository.save(jd);
        log.info("Job description created: {} by recruiter: {}", jd.getId(), recruiterId);

        return jd;
    }

    public List<JobDescription> getRecruiterJobDescriptions(UUID recruiterId) {
        return jobDescriptionRepository.findByRecruiterIdOrderByCreatedAtDesc(recruiterId);
    }

    public List<JobDescription> getActiveJobDescriptions(UUID recruiterId) {
        return jobDescriptionRepository.findActiveByRecruiterId(recruiterId);
    }

    public JobDescription getJobDescription(UUID jdId, UUID recruiterId) {
        return jobDescriptionRepository.findByIdAndRecruiterId(jdId, recruiterId)
            .orElseThrow(() -> ResourceNotFoundException.forEntity("JobDescription", jdId.toString()));
    }

    public JobDescription updateJobDescription(
        UUID jdId,
        UUID recruiterId,
        JobDescriptionRequest request
    ) {
        JobDescription jd = getJobDescription(jdId, recruiterId);

        jd.setJobTitle(request.getJobTitle());
        jd.setCompanyName(request.getCompanyName());
        jd.setDescription(request.getDescription());
        jd.setRequirements(request.getRequirements());
        jd.setPreferences(request.getPreferences());
        jd.setResponsibilities(request.getResponsibilities());
        jd.setLocation(request.getLocation());
        jd.setEmploymentType(request.getEmploymentType());
        jd.setSalaryRange(request.getSalaryRange());

        jd = jobDescriptionRepository.save(jd);
        log.info("Job description updated: {} by recruiter: {}", jdId, recruiterId);

        return jd;
    }

    public void deleteJobDescription(UUID jdId, UUID recruiterId) {
        JobDescription jd = getJobDescription(jdId, recruiterId);
        jobDescriptionRepository.delete(jd);
        log.info("Job description deleted: {} by recruiter: {}", jdId, recruiterId);
    }

    public JobDescription toggleJobDescriptionStatus(UUID jdId, UUID recruiterId, boolean isActive) {
        JobDescription jd = getJobDescription(jdId, recruiterId);
        jd.setIsActive(isActive);
        jd = jobDescriptionRepository.save(jd);
        log.info("Job description status updated: {} - Active: {}", jdId, isActive);

        return jd;
    }
}
