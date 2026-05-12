package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.request.WorkLogRequest;
import com.architect.tasksystem.dto.response.WorkLogResponse;
import com.architect.tasksystem.entity.WorkLog;
import com.architect.tasksystem.exception.ResourceNotFoundException;
import com.architect.tasksystem.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkLogService {
    private final WorkLogRepository workLogRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Transactional
    public WorkLogResponse addLog(Long taskId, WorkLogRequest req, Long userId) {
        LocalDate date = req.getLogDate() != null ? req.getLogDate() : LocalDate.now();
        WorkLog log = WorkLog.builder()
            .task(taskService.getEntityById(taskId))
            .user(userService.getEntityById(userId))
            .logDate(date).description(req.getDescription())
            .plannedForTomorrow(req.getPlannedForTomorrow())
            .blockers(req.getBlockers()).hoursSpent(req.getHoursSpent()).build();
        return WorkLogResponse.from(workLogRepository.save(log));
    }

    public List<WorkLogResponse> getLogsForTask(Long taskId) {
        return workLogRepository.findByTaskIdOrderByLogDateDesc(taskId)
            .stream().map(WorkLogResponse::from).toList();
    }

    public List<WorkLogResponse> getLogsForUser(Long userId) {
        return workLogRepository.findByUserId(userId)
            .stream().map(WorkLogResponse::from).toList();
    }

    public List<WorkLogResponse> getLogsByDateRange(Long userId, LocalDate from, LocalDate to) {
        return workLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateDesc(userId, from, to)
            .stream().map(WorkLogResponse::from).toList();
    }

    public List<WorkLogResponse> getLogsByDate(Long userId, LocalDate date) {
        return workLogRepository.findByUserIdAndLogDate(userId, date)
            .stream().map(WorkLogResponse::from).toList();
    }

    public WorkLogResponse getById(Long id) {
        return WorkLogResponse.from(workLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("WorkLog", id)));
    }
}
