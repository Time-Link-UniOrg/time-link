package com.timelink.time_link.dto.Student;

import java.time.LocalDate;

public record StudentResponseDTO(
        private Long id;
        private String name;
        private Boolean active;
        private LocalDate dateBirth;
        private String username;
        private String groupName;
) {
}
