package com.hiremind.audit.repository;

import com.hiremind.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserId(UUID userId);
    List<AuditLog> findByEntityId(UUID entityId);

    @Query("SELECT al FROM AuditLog al WHERE al.userId = :userId ORDER BY al.createdAt DESC")
    List<AuditLog> findByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);

    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType AND al.entityId = :entityId ORDER BY al.createdAt DESC")
    List<AuditLog> findAuditTrailForEntity(@Param("entityType") String entityType, @Param("entityId") UUID entityId);

    @Query("SELECT al FROM AuditLog al WHERE al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
