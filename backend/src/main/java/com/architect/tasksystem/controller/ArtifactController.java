package com.architect.tasksystem.controller;
import com.architect.tasksystem.security.SecurityUtils;
import com.architect.tasksystem.service.ArtifactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(name = "Artifacts")
@RequiredArgsConstructor
public class ArtifactController {
    private final ArtifactService artifactService;
    private final SecurityUtils securityUtils;

    @PostMapping("/tasks/{taskId}/artifacts")
    public ResponseEntity<?> upload(@PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        return ResponseEntity.ok(artifactService.upload(taskId, file, description, securityUtils.getCurrentUser()));
    }

    @GetMapping("/tasks/{taskId}/artifacts")
    public ResponseEntity<?> getByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(artifactService.getByTask(taskId));
    }

    @GetMapping("/users/{userId}/artifacts")
    public ResponseEntity<?> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(artifactService.getByUser(userId));
    }

    @GetMapping("/artifacts/{id}/download")
    public ResponseEntity<?> getDownloadUrl(@PathVariable Long id) {
        return ResponseEntity.ok(artifactService.getDownloadUrl(id));
    }

    @DeleteMapping("/artifacts/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        artifactService.delete(id, securityUtils.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }
}
