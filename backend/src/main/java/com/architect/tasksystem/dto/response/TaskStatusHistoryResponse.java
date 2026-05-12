package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.TaskStatusHistory;
import com.architect.tasksystem.enums.TaskStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class TaskStatusHistoryResponse {
    private Long id;
    private TaskStatus oldStatus;
    private TaskStatus newStatus;
    private String remarks;
    private UserResponse changedBy;
    private LocalDateTime changedAt;

    public static TaskStatusHistoryResponse from(TaskStatusHistory h) {
        return TaskStatusHistoryResponse.builder()
            .id(h.getId()).oldStatus(h.getOldStatus()).newStatus(h.getNewStatus())
            .remarks(h.getRemarks())
            .changedBy(h.getChangedBy() != null ? UserResponse.from(h.getChangedBy()) : null)
            .changedAt(h.getChangedAt()).build();
    }
}
