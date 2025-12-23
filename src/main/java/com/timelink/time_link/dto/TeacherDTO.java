package com.timelink.time_link.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    private String phone;

    @Email(message = "Email should be valid")
    private String email;

    private String students;
    private String groups;
    private String coursesId;

    @NotBlank(message = "Username is required")
    private String username;
    private String password;
}