package com.architect.tasksystem.repository;
import com.architect.tasksystem.entity.Project;
import com.architect.tasksystem.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findAllByOrderByCreatedAtDesc();
}
