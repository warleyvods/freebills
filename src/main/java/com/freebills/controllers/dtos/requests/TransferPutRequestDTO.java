package com.freebills.controllers.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TransferPutRequestDTO(
        @NotNull
        Long id,
        String observation,
        String description,
        Double amount,
        String transferCategory,
        Long fromAccountId,
        Long toAccountId,
        LocalDateTime createdAt
) {
}
