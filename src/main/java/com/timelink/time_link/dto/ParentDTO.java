package com.timelink.time_link.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentDTO {

    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    private String name;
    private String phone;
    private String email;
    private String child;
    private boolean paid;
    private String username;
}
