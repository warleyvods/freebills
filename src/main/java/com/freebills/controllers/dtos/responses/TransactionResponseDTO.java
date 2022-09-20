package com.freebills.controllers.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        Double amount,
        boolean paid,

        @JsonFormat(pattern="dd-MM-yyyy")
        LocalDate date,
        String description,
        String transactionType,
        String transactionCategory,
        String barCode,
        boolean bankSlip
) {
}
