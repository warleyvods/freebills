package com.freebills.controllers.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UserPutPasswordRequestDTO(

        @Schema(description = "Id", example = "1", required = true)
        @NotNull Long id,

        @Schema(description = "Password", example = "fresh", required = true)
        @NotBlank String password

) {
}
