package com.timelink.time_link.dto.Student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record StudentRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
        String name,

        @NotNull(message = "Active status is required")
        Boolean active,

        LocalDate dateBirth,

        String username,

        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        Long groupId
) {
    public StudentRequestDTO {
        if (name != null) name = name.trim();
        if (username != null && !username.isBlank()) username = username.trim();
    }
}
