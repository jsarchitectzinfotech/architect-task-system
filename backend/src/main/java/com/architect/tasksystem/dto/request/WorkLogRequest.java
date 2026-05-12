package com.architect.tasksystem.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkLogRequest {
    @NotBlank private String description;
    private String plannedForTomorrow;
    private String blockers;
    private Double hoursSpent;
    private LocalDate logDate;
}
