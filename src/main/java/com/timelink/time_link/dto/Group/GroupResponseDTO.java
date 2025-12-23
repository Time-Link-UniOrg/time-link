package com.timelink.time_link.dto.Group;
import java.time.LocalDateTime;
import java.util.List;

public record GroupResponseDTO(
        Long id,
        String name,
        Boolean active,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<Long> studentIds
) {}