package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.Notification;
import com.architect.tasksystem.enums.NotificationType;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String message;
    private Long taskId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
            .id(n.getId()).type(n.getType()).message(n.getMessage())
            .taskId(n.getTaskId()).isRead(n.isRead()).createdAt(n.getCreatedAt()).build();
    }
}
