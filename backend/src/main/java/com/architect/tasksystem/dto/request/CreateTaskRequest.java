package com.architect.tasksystem.dto.request;
import com.architect.tasksystem.enums.TaskPriority;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateTaskRequest {
    @NotBlank private String title;
    private String description;
    @NotNull private Long assignedToId;
    @NotNull private Long projectId;
    private TaskPriority priority = TaskPriority.MEDIUM;
    private LocalDate dueDate;
    private Integer estimatedHours;
}
