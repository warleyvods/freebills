package com.freebills.controllers.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CategoryPostRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String color,
        String icon,
        @NotBlank
        String categoryType
) {
}
