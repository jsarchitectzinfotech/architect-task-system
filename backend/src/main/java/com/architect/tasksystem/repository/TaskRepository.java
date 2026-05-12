package com.architect.tasksystem.repository;
import com.architect.tasksystem.entity.Task;
import com.architect.tasksystem.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);
    List<Task> findByStatus(TaskStatus status);
    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.status NOT IN ('APPROVED')")
    List<Task> findOverdueTasks(@Param("today") LocalDate today);
    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId AND t.dueDate < :today AND t.status NOT IN ('APPROVED')")
    List<Task> findOverdueTasksForUser(@Param("userId") Long userId, @Param("today") LocalDate today);
    List<Task> findByAssignedByIdOrderByCreatedAtDesc(Long managerId);
    long countByAssignedToIdAndStatus(Long userId, TaskStatus status);
}
