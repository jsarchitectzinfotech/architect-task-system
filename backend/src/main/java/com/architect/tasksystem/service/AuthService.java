package com.architect.tasksystem.service;

import com.architect.tasksystem.dto.request.LoginRequest;
import com.architect.tasksystem.dto.request.RegisterRequest;
import com.architect.tasksystem.dto.response.AuthResponse;
import com.architect.tasksystem.dto.response.UserResponse;
import com.architect.tasksystem.entity.User;
import com.architect.tasksystem.repository.UserRepository;
import com.architect.tasksystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = tokenProvider.generateToken(auth);
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        return AuthResponse.builder()
            .token(token).tokenType("Bearer")
            .userId(user.getId()).name(user.getName())
            .email(user.getEmail()).role(user.getRole()).build();
    }

    public UserResponse register(RegisterRequest req) {
        return userService.register(req);
    }
}
