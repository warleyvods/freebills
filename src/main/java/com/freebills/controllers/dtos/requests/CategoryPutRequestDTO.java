package com.freebills.controllers.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryPutRequestDTO(
        @NotNull
        Long id,
        @NotBlank
        String name,
        @NotBlank
        String color,
        String icon,
        @NotBlank
        String categoryType
) {
}
