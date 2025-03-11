package com.freebills.controllers.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDTO(
        Long id,
        Double amount,
        LocalDate date,
        String description,
        String transactionType,
        String transactionCategory,
        String barCode,
        Long accountId,
        Long categoryId,
        String observation,
        UUID receiptId,
        TransactionMetadataResponseDTO metadata
) { }
