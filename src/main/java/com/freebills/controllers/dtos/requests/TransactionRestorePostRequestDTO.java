package com.freebills.controllers.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransactionRestorePostRequestDTO(
        @NotNull
        Long accountId,
        @NotNull
        Double amount,
        boolean paid,
        @NotBlank
        String description,
        @NotNull
        LocalDate date,
        @NotBlank
        String transactionType,
        String transactionCategory,
        @NotNull
        String categoryName,
        String barCode,
        boolean bankSlip,
        String observation
) {
}
