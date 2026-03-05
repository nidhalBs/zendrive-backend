package com.zendrive.backend.service;

import com.zendrive.backend.dto.NotificationDTO;
import com.zendrive.backend.entity.Notification;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.enums.NotificationChannel;
import com.zendrive.backend.enums.NotificationType;
import com.zendrive.backend.mapper.NotificationMapper;
import com.zendrive.backend.repository.NotificationRepository;
import com.zendrive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final JavaMailSender mailSender;

    @Transactional
    public NotificationDTO sendNotification(Long userId, NotificationType type, String subject, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .channel(NotificationChannel.EMAIL)
                .subject(subject)
                .content(content)
                .recipient(user.getEmail())
                .sent(false)
                .read(false)
                .build();

        notification = notificationRepository.save(notification);

        // Send email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
        }

        return notificationMapper.toDTO(notification);
    }

    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable).map(notificationMapper::toDTO);
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId).stream()
                .map(notificationMapper::toDTO).toList();
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    public Page<NotificationDTO> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(notificationMapper::toDTO);
    }
}
