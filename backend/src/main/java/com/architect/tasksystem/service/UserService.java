package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.request.RegisterRequest;
import com.architect.tasksystem.dto.response.UserResponse;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.enums.Role;
import com.architect.tasksystem.exception.ResourceNotFoundException;
import com.architect.tasksystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");
        User user = User.builder()
            .name(req.getName()).email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .role(req.getRole() != null ? req.getRole() : Role.JUNIOR)
            .phone(req.getPhone()).designation(req.getDesignation()).active(true).build();
        return UserResponse.from(userRepository.save(user));
    }

    public List<UserResponse> getAllJuniors() {
        return userRepository.findByRoleAndActiveTrue(Role.JUNIOR)
            .stream().map(UserResponse::from).toList();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream().map(UserResponse::from).toList();
    }

    public UserResponse toggleActive(Long id) {
        User user = getEntityById(id);
        user.setActive(!user.isActive());
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse getById(Long id) {
        return UserResponse.from(userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    public User getEntityById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
