package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.Project;
import com.architect.tasksystem.enums.ProjectStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String clientName;
    private String location;
    private String projectCode;
    private ProjectStatus status;
    private UserResponse createdBy;
    private int taskCount;
    private LocalDateTime createdAt;

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
            .id(project.getId()).name(project.getName()).description(project.getDescription())
            .clientName(project.getClientName()).location(project.getLocation())
            .projectCode(project.getProjectCode()).status(project.getStatus())
            .createdBy(project.getCreatedBy() != null ? UserResponse.from(project.getCreatedBy()) : null)
            .taskCount(project.getTasks() != null ? project.getTasks().size() : 0)
            .createdAt(project.getCreatedAt()).build();
    }
}
