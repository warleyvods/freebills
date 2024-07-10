package com.freebills.controllers.dtos.responses;

import com.freebills.gateways.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CCTransactionResponseDTO(
        BigDecimal amount,
        LocalDate date,
        String description,
        TransactionType transactionType,
        Long category,
        Long creditCard,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
