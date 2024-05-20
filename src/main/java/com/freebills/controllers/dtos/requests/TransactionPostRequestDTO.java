package com.freebills.controllers.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransactionPostRequestDTO(
        @NotNull
        Long accountId,

        @NotNull
        Double amount,

        boolean paid,

        @NotBlank
        String description,

        //TODO - fix
        //@JsonFormat(pattern="dd-MM-yyyy")
        @NotNull
        LocalDate date,

        @NotBlank
        String transactionType,

        @NotBlank
        String transactionCategory,

        String barCode,

        boolean bankSlip
) {
}
