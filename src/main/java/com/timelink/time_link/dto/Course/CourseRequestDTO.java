package com.timelink.time_link.dto.Course;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CourseRequestDTO(
        @NotBlank(message = "Course name is required")
        @Size(min = 3, max = 100, message = "Course name should be between 3 and 100 characters")
        String name,

        String teachers,
        String lessons,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Age is required")
        @Min(value = 5, message = "Age must be at least 5")
        @Max(value = 18, message = "Age must be at most 18")
        Integer age,

        @NotNull(message = "Time period is required")
        @Min(value = 1, message = "Time period must be at least 1 month")
        Integer timePeriod
) {
    public CourseRequestDTO {
        if (name != null) name = name.trim();
    }
}