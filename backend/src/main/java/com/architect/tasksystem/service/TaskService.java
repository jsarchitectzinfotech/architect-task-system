package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.request.CreateTaskRequest;
import com.architect.tasksystem.dto.request.UpdateTaskStatusRequest;
import com.architect.tasksystem.dto.response.*;
import com.architect.tasksystem.entity.*;
import com.architect.tasksystem.enums.NotificationType;
import com.architect.tasksystem.enums.TaskStatus;
import com.architect.tasksystem.exception.ResourceNotFoundException;
import com.architect.tasksystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final TaskStatusHistoryRepository statusHistoryRepository;
    private final NotificationService notificationService;
    private final com.architect.tasksystem.repository.ArtifactRepository artifactRepository;
    private final com.architect.tasksystem.storage.StorageService storageService;

    @Transactional
    public TaskResponse create(CreateTaskRequest req, User assignedBy) {
        User assignedTo = userService.getEntityById(req.getAssignedToId());
        Project project = projectService.getEntityById(req.getProjectId());

        Task task = Task.builder()
            .title(req.getTitle()).description(req.getDescription())
            .assignedTo(assignedTo).assignedBy(assignedBy).project(project)
            .priority(req.getPriority()).dueDate(req.getDueDate())
            .estimatedHours(req.getEstimatedHours()).status(TaskStatus.ASSIGNED).build();

        Task saved = taskRepository.save(task);

        notificationService.create(assignedTo, NotificationType.TASK_ASSIGNED,
            "New task assigned: " + task.getTitle(), saved.getId());

        return TaskResponse.from(saved);
    }

    public List<TaskResponse> getAllForUser(Long userId) {
        return taskRepository.findByAssignedToId(userId)
            .stream().map(TaskResponse::from).toList();
    }

    public List<TaskResponse> getAllByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
            .stream().map(TaskResponse::from).toList();
    }

    public List<TaskResponse> getAll() {
        return taskRepository.findAll().stream().map(TaskResponse::from).toList();
    }

    public TaskResponse getById(Long id) {
        return TaskResponse.from(getEntityById(id));
    }

    public Task getEntityById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    @Transactional
    public TaskResponse updateStatus(Long taskId, UpdateTaskStatusRequest req, User changedBy) {
        Task task = getEntityById(taskId);
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(req.getStatus());
        Task saved = taskRepository.save(task);

        statusHistoryRepository.save(TaskStatusHistory.builder()
            .task(saved).changedBy(changedBy)
            .oldStatus(oldStatus).newStatus(req.getStatus())
            .remarks(req.getRemarks()).build());

        // Notify relevant party
        User notify = req.getStatus() == TaskStatus.UNDER_REVIEW ? task.getAssignedBy() : task.getAssignedTo();
        if (notify != null) {
            notificationService.create(notify,
                req.getStatus() == TaskStatus.APPROVED ? NotificationType.TASK_APPROVED : NotificationType.TASK_STATUS_CHANGED,
                "Task '" + task.getTitle() + "' is now " + req.getStatus().name().replace("_"," "),
                taskId);
        }
        return TaskResponse.from(saved);
    }

    @Transactional
    public TaskResponse update(Long id, CreateTaskRequest req) {
        Task task = getEntityById(id);
        if (req.getTitle() != null) task.setTitle(req.getTitle());
        if (req.getDescription() != null) task.setDescription(req.getDescription());
        if (req.getPriority() != null) task.setPriority(req.getPriority());
        if (req.getDueDate() != null) task.setDueDate(req.getDueDate());
        if (req.getEstimatedHours() != null) task.setEstimatedHours(req.getEstimatedHours());
        if (req.getAssignedToId() != null) task.setAssignedTo(userService.getEntityById(req.getAssignedToId()));
        return TaskResponse.from(taskRepository.save(task));
    }

    public List<TaskStatusHistoryResponse> getStatusHistory(Long taskId) {
        return statusHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId)
            .stream().map(TaskStatusHistoryResponse::from).toList();
    }

    public List<TaskResponse> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now())
            .stream().map(TaskResponse::from).toList();
    }

    public List<TaskResponse> getOverdueTasksForUser(Long userId) {
        return taskRepository.findOverdueTasksForUser(userId, LocalDate.now())
            .stream().map(TaskResponse::from).toList();
    }

    @Transactional
    public void delete(Long id) {
        Task task = getEntityById(id);
        artifactRepository.findByTaskIdOrderByCreatedAtDesc(task.getId()).forEach(a -> {
            try { storageService.deleteFile(a.getStoragePath()); } catch (Exception ignored) {}
        });
        taskRepository.delete(task);
    }
}
