package com.timelink.time_link.dto.Substitute;

import jakarta.validation.constraints.NotNull;

public record SubstituteRequestDTO(
        @NotNull(message = "Lesson ID is required")
        Integer lessonId
) {}
