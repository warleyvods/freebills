package com.freebills.controllers.dtos.responses;

import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        Double amount,
        boolean paid,
        LocalDate date,
        String description,
        String transactionType,
        String transactionCategory,
        String barCode,
        boolean bankSlip,
        Long accountId,
        Long categoryId,
        String observation
) { }
