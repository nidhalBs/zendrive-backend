package com.zendrive.backend.dto;

import com.zendrive.backend.enums.NotificationChannel;
import com.zendrive.backend.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private Long userId;
    private NotificationType type;
    private NotificationChannel channel;
    private String subject;
    private String content;
    private String recipient;
    private boolean sent;
    private boolean read;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
