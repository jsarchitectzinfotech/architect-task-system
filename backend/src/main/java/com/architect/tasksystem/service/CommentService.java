package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.request.CommentRequest;
import com.architect.tasksystem.dto.response.CommentResponse;
import com.architect.tasksystem.entity.Comment;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.NotificationType;
import com.architect.tasksystem.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final NotificationService notificationService;

    @Transactional
    public CommentResponse addComment(Long taskId, CommentRequest req, User author) {
        var task = taskService.getEntityById(taskId);
        Comment comment = Comment.builder()
            .task(task).author(author).content(req.getContent()).build();
        Comment saved = commentRepository.save(comment);

        // Notify the other party
        User notify = author.getId().equals(task.getAssignedTo().getId())
            ? task.getAssignedBy() : task.getAssignedTo();
        if (notify != null) {
            notificationService.create(notify, NotificationType.COMMENT_ADDED,
                author.getName() + " commented on: " + task.getTitle(), taskId);
        }
        return CommentResponse.from(saved);
    }

    public List<CommentResponse> getByTask(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId)
            .stream().map(CommentResponse::from).toList();
    }
}
