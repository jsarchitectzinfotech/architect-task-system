package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.response.ArtifactResponse;
import com.architect.tasksystem.entity.Artifact;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.NotificationType;
import com.architect.tasksystem.exception.ResourceNotFoundException;
import com.architect.tasksystem.repository.ArtifactRepository;
import com.architect.tasksystem.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtifactService {
    private final ArtifactRepository artifactRepository;
    private final StorageService storageService;
    private final TaskService taskService;
    private final NotificationService notificationService;

    @Transactional
    public ArtifactResponse upload(Long taskId, MultipartFile file, String description, User uploader) {
        String storagePath = storageService.uploadFile(file, taskId, uploader.getId());
        String fileUrl = storageService.getPublicUrl(storagePath);

        Artifact artifact = Artifact.builder()
            .task(taskService.getEntityById(taskId))
            .uploadedBy(uploader)
            .fileName(file.getOriginalFilename())
            .fileUrl(fileUrl).storagePath(storagePath)
            .fileType(file.getContentType()).fileSize(file.getSize())
            .description(description).build();
        Artifact saved = artifactRepository.save(artifact);

        var task = taskService.getEntityById(taskId);
        if (task.getAssignedBy() != null && !task.getAssignedBy().getId().equals(uploader.getId())) {
            notificationService.create(task.getAssignedBy(), NotificationType.ARTIFACT_UPLOADED,
                uploader.getName() + " uploaded a file to task: " + task.getTitle(), taskId);
        }
        return ArtifactResponse.from(saved);
    }

    public List<ArtifactResponse> getByTask(Long taskId) {
        return artifactRepository.findByTaskIdOrderByCreatedAtDesc(taskId)
            .stream().map(ArtifactResponse::from).toList();
    }

    public List<ArtifactResponse> getByUser(Long userId) {
        return artifactRepository.findByUploadedByIdOrderByCreatedAtDesc(userId)
            .stream().map(ArtifactResponse::from).toList();
    }

    public String getDownloadUrl(Long artifactId) {
        Artifact a = artifactRepository.findById(artifactId)
            .orElseThrow(() -> new ResourceNotFoundException("Artifact", artifactId));
        return storageService.getSignedUrl(a.getStoragePath());
    }

    @Transactional
    public void delete(Long artifactId, Long userId) {
        Artifact a = artifactRepository.findById(artifactId)
            .orElseThrow(() -> new ResourceNotFoundException("Artifact", artifactId));
        storageService.deleteFile(a.getStoragePath());
        artifactRepository.delete(a);
    }
}
