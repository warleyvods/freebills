package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransactionPutRequestDTO(
        @NotNull
        Long accountId,
        @NotNull
        Long id,
        @NotNull
        Double amount,
        boolean paid,
        @NotBlank
        String description,
        @JsonFormat(pattern="dd-MM-yyyy")
        @NotNull
        LocalDate date,
        @NotBlank
        String transactionType,
        String transactionCategory,
        @NotNull
        Long categoryId,
        boolean bankSlip,
        String barCode
) { }
