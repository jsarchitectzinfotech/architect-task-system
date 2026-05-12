package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.request.CreateProjectRequest;
import com.architect.tasksystem.dto.response.ProjectResponse;
import com.architect.tasksystem.entity.Project;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.ProjectStatus;
import com.architect.tasksystem.exception.ResourceNotFoundException;
import com.architect.tasksystem.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse create(CreateProjectRequest req, User creator) {
        Project project = Project.builder()
            .name(req.getName()).description(req.getDescription())
            .clientName(req.getClientName()).location(req.getLocation())
            .projectCode(req.getProjectCode()).status(ProjectStatus.ACTIVE)
            .createdBy(creator).build();
        return ProjectResponse.from(projectRepository.save(project));
    }

    public List<ProjectResponse> getAll() {
        return projectRepository.findAllByOrderByCreatedAtDesc()
            .stream().map(ProjectResponse::from).toList();
    }

    public ProjectResponse getById(Long id) {
        return ProjectResponse.from(projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project", id)));
    }

    public Project getEntityById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    @Transactional
    public ProjectResponse updateStatus(Long id, ProjectStatus status) {
        Project project = getEntityById(id);
        project.setStatus(status);
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse update(Long id, CreateProjectRequest req) {
        Project project = getEntityById(id);
        if (req.getName() != null) project.setName(req.getName());
        if (req.getDescription() != null) project.setDescription(req.getDescription());
        if (req.getClientName() != null) project.setClientName(req.getClientName());
        if (req.getLocation() != null) project.setLocation(req.getLocation());
        if (req.getProjectCode() != null) project.setProjectCode(req.getProjectCode());
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.delete(getEntityById(id));
    }
}
