package com.timelink.time_link.dto.Lesson;

import com.timelink.time_link.model.LessonStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record LessonRequestDTO(
        @NotNull(message = "Lesson date is required")
        LocalDate lessonDate,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        LocalTime endTime,

        @NotNull(message = "Original teacher ID is required")
        Integer originalTeacherId,

        Integer actualTeacherId,

        @NotNull(message = "Group ID is required")
        Integer groupId,

        @NotNull(message = "Course ID is required")
        Integer courseId,

        String notes,

        LessonStatus status
) {
    public LessonRequestDTO {
        if (notes != null) notes = notes.trim();
    }
}