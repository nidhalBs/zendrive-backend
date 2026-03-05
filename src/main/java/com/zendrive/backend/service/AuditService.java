package com.zendrive.backend.service;

import com.zendrive.backend.entity.AuditLog;
import com.zendrive.backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String action, String entityType, Long entityId, Long performedBy, String performedByEmail, String details) {
        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .performedBy(performedBy)
                .performedByEmail(performedByEmail)
                .details(details)
                .build();
        auditLogRepository.save(auditLog);
    }

    public Page<AuditLog> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    public Page<AuditLog> getAuditLogsByEntity(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityType(entityType, pageable);
    }

    public Page<AuditLog> getAuditLogsByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByPerformedBy(userId, pageable);
    }
}
