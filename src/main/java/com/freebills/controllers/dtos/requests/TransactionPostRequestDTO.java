package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransactionPostRequestDTO(
        @NotNull
        Double amount,
        @JsonFormat(pattern="dd-MM-yyyy")
        @NotNull
        LocalDate date,
        @NotBlank
        String description,
        @NotBlank
        String transactionType,
        @NotNull
        Long categoryId,
        String barCode,
        @NotNull
        Long accountId,
        String observation,
        TransactionMetadataRequestDTO metadata
) {
}
