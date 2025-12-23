package com.timelink.time_link.dto.Teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeacherRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
        String name,

        String phone,

        @Email(message = "Email should be valid")
        String email,

        String students,
        String groups,
        String coursesId,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters")
        String username,

        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {
    public TeacherRequestDTO {
        if (name != null) name = name.trim();
        if (username != null) username = username.trim();
    }
}