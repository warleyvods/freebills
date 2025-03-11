package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionPutRequestDTO(
        @NotNull
        Long accountId,
        @NotNull
        Long id,
        @NotNull
        Double amount,
        @JsonFormat(pattern="dd-MM-yyyy")
        @NotNull
        LocalDate date,
        @NotBlank
        String description,
        @NotBlank
        String transactionType,
        String transactionCategory,
        @NotNull
        Long categoryId,
        String barCode,
        String observation,
        UUID receiptId,
        TransactionMetadataRequestDTO metadata
) { }
