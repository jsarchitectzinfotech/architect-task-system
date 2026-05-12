package com.architect.tasksystem.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank private String name;
    private String description;
    private String clientName;
    private String location;
    private String projectCode;
}
