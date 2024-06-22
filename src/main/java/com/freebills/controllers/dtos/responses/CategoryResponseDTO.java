package com.freebills.controllers.dtos.responses;

public record CategoryResponseDTO(
        Long id,
        String name,
        String color,
        String createdAt,
        String updatedAt
) {
}
