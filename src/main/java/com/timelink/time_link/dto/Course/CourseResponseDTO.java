package com.timelink.time_link.dto.Course;

import java.math.BigDecimal;

public record CourseResponseDTO(
        Integer id,
        String name,
        String teachers,
        String lessons,
        BigDecimal price,
        Integer age,
        Integer timePeriod
) {}