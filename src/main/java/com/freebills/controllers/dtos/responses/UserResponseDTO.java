package com.freebills.controllers.dtos.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UserResponseDTO(
        Long id,
        String name,
        String login,
        String email,
        boolean admin,
        boolean active,
        String imgUrl,
        LocalDate createdAt,
        LocalDateTime lastAccess
) {

}
