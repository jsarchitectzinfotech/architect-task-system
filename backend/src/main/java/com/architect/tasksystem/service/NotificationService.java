package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.response.NotificationResponse;
import com.architect.tasksystem.entity.Notification;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.NotificationType;
import com.architect.tasksystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void create(User user, NotificationType type, String message, Long taskId) {
        notificationRepository.save(Notification.builder()
            .user(user).type(type).message(message).taskId(taskId).isRead(false).build());
    }

    public List<NotificationResponse> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .stream().map(NotificationResponse::from).toList();
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markRead(Long notificationId, Long userId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            if (n.getUser().getId().equals(userId)) { n.setRead(true); notificationRepository.save(n); }
        });
    }

    @Transactional
    public void markAllRead(Long userId) {
        notificationRepository.markAllReadForUser(userId);
    }
}
