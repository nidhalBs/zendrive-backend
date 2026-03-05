package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Notification;
import com.zendrive.backend.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    List<Notification> findByUserIdAndReadFalse(Long userId);
    long countByUserIdAndReadFalse(Long userId);
    Page<Notification> findByType(NotificationType type, Pageable pageable);
}
