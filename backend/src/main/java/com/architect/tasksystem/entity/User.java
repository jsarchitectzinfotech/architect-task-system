package com.architect.tasksystem.entity;

import com.architect.tasksystem.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"assignedTasks", "createdTasks", "workLogs", "artifacts", "comments", "notifications"})
@ToString(exclude = {"assignedTasks", "createdTasks", "workLogs", "artifacts", "comments", "notifications"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String phone;
    private String designation;
    private boolean active = true;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "assignedBy", fetch = FetchType.LAZY)
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<WorkLog> workLogs;

    @OneToMany(mappedBy = "uploadedBy", fetch = FetchType.LAZY)
    private List<Artifact> artifacts;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;
}
