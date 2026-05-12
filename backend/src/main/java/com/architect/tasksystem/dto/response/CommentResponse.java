package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.Comment;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CommentResponse {
    private Long id;
    private Long taskId;
    private UserResponse author;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment c) {
        return CommentResponse.builder()
            .id(c.getId()).taskId(c.getTask().getId())
            .author(UserResponse.from(c.getAuthor()))
            .content(c.getContent()).createdAt(c.getCreatedAt()).build();
    }
}
