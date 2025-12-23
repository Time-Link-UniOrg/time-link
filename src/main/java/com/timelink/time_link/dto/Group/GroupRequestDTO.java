package com.timelink.time_link.dto.Group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record GroupRequestDTO(
        @NotBlank(message = "Group name is required")
        @Size(min = 3, max = 100, message = "Group name should be between 3 and 100 characters")
        String name,

        @NotNull(message = "Active status is required")
        Boolean active,

        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public GroupRequestDTO {
        if (name != null) name = name.trim();
    }
}