package com.zendrive.backend.controller;

import com.zendrive.backend.dto.ApiResponse;
import com.zendrive.backend.dto.NotificationDTO;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationDTO>>> getMyNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserNotifications(user.getId(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getUnreadNotifications(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadNotifications(user.getId())));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadCount(user.getId())));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<String>> markAllAsRead(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
}
