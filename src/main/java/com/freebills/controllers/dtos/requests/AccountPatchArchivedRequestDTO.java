package com.freebills.controllers.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


public record AccountPatchArchivedRequestDTO(

        @Schema(description = "Id", example = "1", required = true)
        @NotNull Long id,

        @Schema(description = "Password", example = "fresh", required = true)
        boolean archived

) {
}
