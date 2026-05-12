package com.architect.tasksystem.dto.request;
import com.architect.tasksystem.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {
    @NotNull private TaskStatus status;
    private String remarks;
}
