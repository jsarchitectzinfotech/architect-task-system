package com.architect.tasksystem.dto.response;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DashboardResponse {
    private long totalTasks;
    private long assignedTasks;
    private long inProgressTasks;
    private long underReviewTasks;
    private long approvedTasks;
    private long overdueTasks;
    private List<TaskResponse> recentTasks;
    private List<EmployeeSummary> employeeSummaries;

    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class EmployeeSummary {
        private UserResponse user;
        private long assigned;
        private long inProgress;
        private long underReview;
        private long approved;
        private long overdue;
    }
}
