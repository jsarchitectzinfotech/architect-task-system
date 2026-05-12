package com.architect.tasksystem.controller;
import com.architect.tasksystem.security.SecurityUtils;
import com.architect.tasksystem.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(notificationService.getMyNotifications(securityUtils.getCurrentUser().getId()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount() {
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(securityUtils.getCurrentUser().getId())));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        notificationService.markRead(id, securityUtils.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/mark-all-read")
    public ResponseEntity<?> markAllRead() {
        notificationService.markAllRead(securityUtils.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }
}
