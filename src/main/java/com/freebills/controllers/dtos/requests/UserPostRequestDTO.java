package com.freebills.controllers.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record UserPostRequestDTO(
        @Schema(description = "User name application", example = "Pudge Silva", required = true)
        @NotBlank String name,

        @Schema(description = "Login Application", example = "pudge", required = true)
        @NotBlank String login,

        @Schema(description = "Password", example = "fresh", required = true)
        @NotBlank String password,

        @Schema(description = "Email", example = "dota@dota.com", required = true)
        @NotBlank String email,

        @Schema(description = "admin system", example = "false", required = true)
        boolean admin,

        @Schema(description = "user active", example = "true", required = true)
        boolean active
) {
}
