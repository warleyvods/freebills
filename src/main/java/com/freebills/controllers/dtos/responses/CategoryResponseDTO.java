package com.freebills.controllers.dtos.responses;

public record CategoryResponseDTO(
        Long id,
        String name,
        String color,
        String icon,
        String categoryType,
        Boolean archived,
        String createdAt,
        String updatedAt
) {
}
