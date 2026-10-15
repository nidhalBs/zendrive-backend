package com.zendrive.backend.repository;

import com.zendrive.backend.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);
    Page<AuditLog> findByPerformedBy(Long performedBy, Pageable pageable);
}
