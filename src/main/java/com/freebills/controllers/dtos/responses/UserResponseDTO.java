package com.freebills.controllers.dtos.responses;

import java.time.LocalDate;

public record UserResponseDTO(
        Long id,
        String name,
        String login,
        String email,
        boolean admin,
        boolean active,
        LocalDate createdAt
) {

}
