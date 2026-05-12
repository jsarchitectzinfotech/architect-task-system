package com.architect.tasksystem.dto.request;
import com.architect.tasksystem.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String name;
    @Email @NotBlank private String email;
    @NotBlank @Size(min=6) private String password;
    private Role role = Role.JUNIOR;
    private String phone;
    private String designation;
}
