package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.response.*;
import com.architect.tasksystem.enums.Role;
import com.architect.tasksystem.enums.TaskStatus;
import com.architect.tasksystem.repository.TaskRepository;
import com.architect.tasksystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public DashboardResponse getManagerDashboard() {
        var allTasks = taskRepository.findAll();
        var juniors = userRepository.findByRoleAndActiveTrue(Role.JUNIOR);

        List<DashboardResponse.EmployeeSummary> summaries = juniors.stream().map(u ->
            DashboardResponse.EmployeeSummary.builder()
                .user(UserResponse.from(u))
                .assigned(taskRepository.countByAssignedToIdAndStatus(u.getId(), TaskStatus.ASSIGNED))
                .inProgress(taskRepository.countByAssignedToIdAndStatus(u.getId(), TaskStatus.IN_PROGRESS))
                .underReview(taskRepository.countByAssignedToIdAndStatus(u.getId(), TaskStatus.UNDER_REVIEW))
                .approved(taskRepository.countByAssignedToIdAndStatus(u.getId(), TaskStatus.APPROVED))
                .overdue(taskRepository.findOverdueTasksForUser(u.getId(), LocalDate.now()).size())
                .build()
        ).toList();

        var recent = taskRepository.findAll().stream()
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(10).map(TaskResponse::from).toList();

        return DashboardResponse.builder()
            .totalTasks(allTasks.size())
            .assignedTasks(allTasks.stream().filter(t -> t.getStatus() == TaskStatus.ASSIGNED).count())
            .inProgressTasks(allTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count())
            .underReviewTasks(allTasks.stream().filter(t -> t.getStatus() == TaskStatus.UNDER_REVIEW).count())
            .approvedTasks(allTasks.stream().filter(t -> t.getStatus() == TaskStatus.APPROVED).count())
            .overdueTasks(taskRepository.findOverdueTasks(LocalDate.now()).size())
            .recentTasks(recent).employeeSummaries(summaries).build();
    }

    public DashboardResponse getEmployeeDashboard(Long userId) {
        var tasks = taskRepository.findByAssignedToId(userId);
        var recent = tasks.stream()
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(5).map(TaskResponse::from).toList();
        return DashboardResponse.builder()
            .totalTasks(tasks.size())
            .assignedTasks(tasks.stream().filter(t -> t.getStatus() == TaskStatus.ASSIGNED).count())
            .inProgressTasks(tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count())
            .underReviewTasks(tasks.stream().filter(t -> t.getStatus() == TaskStatus.UNDER_REVIEW).count())
            .approvedTasks(tasks.stream().filter(t -> t.getStatus() == TaskStatus.APPROVED).count())
            .overdueTasks(taskRepository.findOverdueTasksForUser(userId, LocalDate.now()).size())
            .recentTasks(recent).build();
    }
}
