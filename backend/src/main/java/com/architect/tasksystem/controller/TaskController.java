package com.architect.tasksystem.controller;
import com.architect.tasksystem.dto.request.CreateTaskRequest;
import com.architect.tasksystem.dto.request.UpdateTaskStatusRequest;
import com.architect.tasksystem.security.SecurityUtils;
import com.architect.tasksystem.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<?> getTasks() {
        var user = securityUtils.getCurrentUser();
        return ResponseEntity.ok(switch(user.getRole()) {
            case MANAGER -> taskService.getAll();
            case JUNIOR -> taskService.getAllForUser(user.getId());
        });
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getStatusHistory(id));
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdue() {
        var user = securityUtils.getCurrentUser();
        return ResponseEntity.ok(switch(user.getRole()) {
            case MANAGER -> taskService.getOverdueTasks();
            case JUNIOR -> taskService.getOverdueTasksForUser(user.getId());
        });
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody CreateTaskRequest req) {
        return ResponseEntity.ok(taskService.create(req, securityUtils.getCurrentUser()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateTaskRequest req) {
        return ResponseEntity.ok(taskService.update(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateTaskStatusRequest req) {
        return ResponseEntity.ok(taskService.updateStatus(id, req, securityUtils.getCurrentUser()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
