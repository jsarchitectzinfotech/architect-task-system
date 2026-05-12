package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.Task;
import com.architect.tasksystem.enums.TaskPriority;
import com.architect.tasksystem.enums.TaskStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Integer estimatedHours;
    private UserResponse assignedTo;
    private UserResponse assignedBy;
    private Long projectId;
    private String projectName;
    private int commentCount;
    private int artifactCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        return TaskResponse.builder()
            .id(task.getId()).title(task.getTitle()).description(task.getDescription())
            .status(task.getStatus()).priority(task.getPriority()).dueDate(task.getDueDate())
            .estimatedHours(task.getEstimatedHours())
            .assignedTo(task.getAssignedTo() != null ? UserResponse.from(task.getAssignedTo()) : null)
            .assignedBy(task.getAssignedBy() != null ? UserResponse.from(task.getAssignedBy()) : null)
            .projectId(task.getProject() != null ? task.getProject().getId() : null)
            .projectName(task.getProject() != null ? task.getProject().getName() : null)
            .commentCount(task.getComments() != null ? task.getComments().size() : 0)
            .artifactCount(task.getArtifacts() != null ? task.getArtifacts().size() : 0)
            .createdAt(task.getCreatedAt()).updatedAt(task.getUpdatedAt()).build();
    }
}
