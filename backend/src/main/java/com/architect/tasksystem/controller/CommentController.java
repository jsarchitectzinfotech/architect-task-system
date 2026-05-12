package com.architect.tasksystem.controller;
import com.architect.tasksystem.dto.request.CommentRequest;
import com.architect.tasksystem.security.SecurityUtils;
import com.architect.tasksystem.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@Tag(name = "Comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<?> getByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getByTask(taskId));
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable Long taskId, @Valid @RequestBody CommentRequest req) {
        return ResponseEntity.ok(commentService.addComment(taskId, req, securityUtils.getCurrentUser()));
    }
}
