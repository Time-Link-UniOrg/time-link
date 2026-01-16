package com.timelink.time_link.dto.Substitute;

import java.time.LocalDate;
import java.time.LocalTime;

public record SubstituteOpportunityDTO(
        Integer lessonId,
        LocalDate lessonDate,
        LocalTime startTime,
        LocalTime endTime,
        Integer courseId,
        String courseName,
        Integer groupId,
        String groupName,
        Integer teacherId,
        String teacherName
) {}
