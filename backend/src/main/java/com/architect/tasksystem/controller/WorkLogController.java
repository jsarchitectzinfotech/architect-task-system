package com.architect.tasksystem.controller;
import com.architect.tasksystem.dto.request.WorkLogRequest;
import com.architect.tasksystem.security.SecurityUtils;
import com.architect.tasksystem.service.WorkLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@Tag(name = "Work Logs")
@RequiredArgsConstructor
public class WorkLogController {
    private final WorkLogService workLogService;
    private final SecurityUtils securityUtils;

    @PostMapping("/tasks/{taskId}/worklogs")
    public ResponseEntity<?> addLog(@PathVariable Long taskId, @Valid @RequestBody WorkLogRequest req) {
        return ResponseEntity.ok(workLogService.addLog(taskId, req, securityUtils.getCurrentUser().getId()));
    }

    @GetMapping("/tasks/{taskId}/worklogs")
    public ResponseEntity<?> getByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(workLogService.getLogsForTask(taskId));
    }

    @GetMapping("/users/{userId}/worklogs")
    public ResponseEntity<?> getByUser(@PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from != null && to != null) return ResponseEntity.ok(workLogService.getLogsByDateRange(userId, from, to));
        return ResponseEntity.ok(workLogService.getLogsForUser(userId));
    }

    @GetMapping("/worklogs/my")
    public ResponseEntity<?> getMy() {
        return ResponseEntity.ok(workLogService.getLogsForUser(securityUtils.getCurrentUser().getId()));
    }

    @GetMapping("/worklogs/today")
    public ResponseEntity<?> getToday() {
        return ResponseEntity.ok(workLogService.getLogsByDate(securityUtils.getCurrentUser().getId(), LocalDate.now()));
    }
}
