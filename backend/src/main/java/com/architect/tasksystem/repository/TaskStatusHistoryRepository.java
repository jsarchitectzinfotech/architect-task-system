package com.architect.tasksystem.repository;
import com.architect.tasksystem.entity.TaskStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TaskStatusHistoryRepository extends JpaRepository<TaskStatusHistory, Long> {
    List<TaskStatusHistory> findByTaskIdOrderByChangedAtDesc(Long taskId);
}
