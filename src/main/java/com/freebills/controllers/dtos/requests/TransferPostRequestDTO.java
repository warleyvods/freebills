package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransferPostRequestDTO(
        @NotNull
        Double amount,
        @NotNull
        Long toAccountId,
        @JsonFormat(pattern="dd-MM-yyyy")
        LocalDate date,
        @NotNull
        Long fromAccountId,
        String observation
) {
}
