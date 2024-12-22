package com.freebills.controllers.dtos.responses;

import java.time.LocalDate;

public record TransactionRestoreResponseDTO(
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
        String categoryName,
        String observation
) { }
