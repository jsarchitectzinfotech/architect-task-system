package com.architect.tasksystem.repository;
import com.architect.tasksystem.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    List<WorkLog> findByTaskId(Long taskId);
    List<WorkLog> findByUserId(Long userId);
    List<WorkLog> findByUserIdAndLogDate(Long userId, LocalDate date);
    List<WorkLog> findByUserIdAndLogDateBetweenOrderByLogDateDesc(Long userId, LocalDate from, LocalDate to);
    List<WorkLog> findByTaskIdOrderByLogDateDesc(Long taskId);
    boolean existsByTaskIdAndUserIdAndLogDate(Long taskId, Long userId, LocalDate date);
}
