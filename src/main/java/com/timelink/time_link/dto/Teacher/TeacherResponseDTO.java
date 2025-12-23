package com.timelink.time_link.dto.Teacher;

public record TeacherResponseDTO(
        Integer id,
        String name,
        String phone,
        String email,
        String students,
        String groups,
        String coursesId,
        String username
) {}
