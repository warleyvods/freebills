package com.freebills.controllers.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AccountPutRequestDTO(


        @NotNull
        Long id,

        @Schema(description = "Amount ")
        @NotNull
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
        @NotNull
        Long userId
) {

}
