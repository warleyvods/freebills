package com.freebills.controllers.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public record AccountPostRequestDTO(
        @Schema(description = "Amount ")
        Double amount,

        @Schema(description = "Description of the account")
        @NotBlank
        String description,

        @Schema(description = "Type of the account")
        @NotBlank
        String accountType,

        @Schema(description = "Where all the information about the account is displayed")
        boolean dashboard,

        @Schema(description = "Type of the bank")
        @NotBlank
        String bankType,

        @Schema(description = "Id of the user logged")
        Long userId
) {

}
