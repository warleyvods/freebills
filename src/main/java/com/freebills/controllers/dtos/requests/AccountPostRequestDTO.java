package com.freebills.controllers.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record AccountPostRequestDTO(
        @Schema(description = "Amount ")
//        @NotNull
        double amount,

        @Schema(description = "Description of the account")
        @NotBlank
        String description,

        @Schema(description = "Type of the account")
        @NotBlank
        String accountType,

        @Schema(description = "Where all the information about the account is displayed")
        boolean dashboard,

        boolean archived,

        @Schema(description = "Type of the bank")
        @NotBlank
        String bankType
) { }
