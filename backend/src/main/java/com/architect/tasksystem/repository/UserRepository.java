package com.architect.tasksystem.repository;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndActiveTrue(Role role);
    List<User> findByActiveTrue();
}
