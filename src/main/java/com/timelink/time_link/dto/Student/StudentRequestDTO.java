package com.timelink.time_link.dto.Student;

import java.time.LocalDate;

public record StudentRequestDTO(
        private String name,
        private Boolean active,
        private LocalDate dateBirth,
        private String username,
        private String password,
        private Long groupId
) {
}
