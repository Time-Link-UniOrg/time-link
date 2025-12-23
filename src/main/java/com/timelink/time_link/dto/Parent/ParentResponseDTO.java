package com.timelink.time_link.dto.Parent;

public record ParentResponseDTO(
        Integer id,
        String name,
        String phone,
        String email,
        String child,
        boolean paid,
        String username
) {}