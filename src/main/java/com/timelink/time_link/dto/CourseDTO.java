package com.timelink.time_link.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Integer id;

    @NotBlank(message = "Course name is required")
    private String name;

    private String teachers;
    private String lessons;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private BigDecimal price;

    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 18, message = "Age must be at most 18")
    private Integer age;

    @Min(value = 1, message = "Time period must be at least 1 month")
    private Integer timePeriod;
}