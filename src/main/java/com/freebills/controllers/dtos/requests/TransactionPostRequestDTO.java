package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(pattern = "dd-MM-yyyy")
        @NotNull
        LocalDate date,
        @NotBlank
        String transactionType,
        String transactionCategory,
        @NotNull
        Long categoryId,
        String barCode,
        String observation,
        TransactionMetadataRequestDTO transactionMetadata
) {
}
