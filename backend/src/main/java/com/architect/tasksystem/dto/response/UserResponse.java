package com.architect.tasksystem.dto.response;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.Role;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private String designation;
    private boolean active;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId()).name(user.getName()).email(user.getEmail())
            .role(user.getRole()).phone(user.getPhone()).designation(user.getDesignation())
            .active(user.isActive()).createdAt(user.getCreatedAt()).build();
    }
}
