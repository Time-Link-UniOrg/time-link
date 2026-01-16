package com.timelink.time_link.dto.Substitute;

import com.timelink.time_link.model.SubstituteRequestStatus;

import java.time.LocalDateTime;

public record SubstituteResponseDTO(
        Integer requestId,
        Integer lessonId,
        Integer originalTeacherId,
        Integer assignedTeacherId,
        SubstituteRequestStatus status,
        LocalDateTime createdAt
) {}
