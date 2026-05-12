package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.Artifact;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArtifactResponse {
    private Long id;
    private Long taskId;
    private String taskTitle;
    private UserResponse uploadedBy;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private Integer version;
    private String description;
    private LocalDateTime createdAt;

    public static ArtifactResponse from(Artifact a) {
        return ArtifactResponse.builder()
            .id(a.getId()).taskId(a.getTask().getId()).taskTitle(a.getTask().getTitle())
            .uploadedBy(UserResponse.from(a.getUploadedBy())).fileName(a.getFileName())
            .fileUrl(a.getFileUrl()).fileType(a.getFileType()).fileSize(a.getFileSize())
            .version(a.getVersion()).description(a.getDescription()).createdAt(a.getCreatedAt()).build();
    }
}
