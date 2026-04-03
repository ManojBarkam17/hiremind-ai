package com.hiremind.audit.service;

import com.hiremind.audit.entity.AuditLog;
import com.hiremind.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(
        UUID userId,
        String action,
        String entityType,
        UUID entityId,
        String details,
        String ipAddress,
        String userAgent
    ) {
        AuditLog log = AuditLog.builder()
            .userId(userId)
            .action(action)
            .entityType(entityType)
            .entityId(entityId)
            .details(details)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .build();

        auditLogRepository.save(log);
        log.info("Audit log created: {} - {} on {}", userId, action, entityType);
    }

    public List<AuditLog> getUserAuditLog(UUID userId) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<AuditLog> getEntityAuditTrail(String entityType, UUID entityId) {
        return auditLogRepository.findAuditTrailForEntity(entityType, entityId);
    }

    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByDateRange(startDate, endDate);
    }

    public void logResume(UUID userId, String action, UUID resumeId, String details, String ipAddress, String userAgent) {
        logAction(userId, action, "RESUME", resumeId, details, ipAddress, userAgent);
    }

    public void logAnalysis(UUID userId, String action, UUID analysisId, String details, String ipAddress, String userAgent) {
        logAction(userId, action, "ANALYSIS", analysisId, details, ipAddress, userAgent);
    }

    public void logOptimization(UUID userId, String action, UUID optimizationId, String details, String ipAddress, String userAgent) {
        logAction(userId, action, "OPTIMIZATION", optimizationId, details, ipAddress, userAgent);
    }

    public void logJobDescription(UUID userId, String action, UUID jdId, String details, String ipAddress, String userAgent) {
        logAction(userId, action, "JOB_DESCRIPTION", jdId, details, ipAddress, userAgent);
    }

    public void logAuthentication(UUID userId, String action, String ipAddress, String userAgent) {
        logAction(userId, action, "AUTH", null, "User authentication event", ipAddress, userAgent);
    }
}
