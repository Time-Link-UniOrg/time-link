package com.timelink.time_link.dto.Lesson;

import com.timelink.time_link.model.LessonStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record LessonResponseDTO(
        Integer id,
        LocalDate lessonDate,
        LocalTime startTime,
        LocalTime endTime,

        Integer originalTeacherId,
        String originalTeacherName,

        Integer actualTeacherId,
        String actualTeacherName,

        Integer groupId,
        String groupName,

        Integer courseId,
        String courseName,

        String notes,
        LessonStatus status,
        Boolean hasSubstitute
) {
}