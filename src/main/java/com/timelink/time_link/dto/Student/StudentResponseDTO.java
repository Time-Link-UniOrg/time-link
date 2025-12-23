package com.timelink.time_link.dto.Student;

import java.time.LocalDate;

public record StudentResponseDTO(
        Long id,
        String name,
        Boolean active,
        LocalDate dateBirth,
        String username,
        Long groupId
) {}
