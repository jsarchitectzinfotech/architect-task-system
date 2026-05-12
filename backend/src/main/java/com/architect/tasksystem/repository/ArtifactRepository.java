package com.architect.tasksystem.repository;
import com.architect.tasksystem.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    List<Artifact> findByTaskIdOrderByCreatedAtDesc(Long taskId);
    List<Artifact> findByUploadedByIdOrderByCreatedAtDesc(Long userId);
}
