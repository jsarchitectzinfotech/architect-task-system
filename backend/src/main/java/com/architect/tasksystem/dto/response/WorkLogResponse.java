package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.WorkLog;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class WorkLogResponse {
    private Long id;
    private Long taskId;
    private String taskTitle;
    private UserResponse user;
    private LocalDate logDate;
    private String description;
    private String plannedForTomorrow;
    private String blockers;
    private Double hoursSpent;
    private LocalDateTime createdAt;

    public static WorkLogResponse from(WorkLog w) {
        return WorkLogResponse.builder()
            .id(w.getId()).taskId(w.getTask().getId()).taskTitle(w.getTask().getTitle())
            .user(UserResponse.from(w.getUser())).logDate(w.getLogDate())
            .description(w.getDescription()).plannedForTomorrow(w.getPlannedForTomorrow())
            .blockers(w.getBlockers()).hoursSpent(w.getHoursSpent()).createdAt(w.getCreatedAt()).build();
    }
}
