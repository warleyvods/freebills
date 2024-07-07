package com.freebills.controllers.dtos.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransferResponseDTO(
       Long id,
       String observation,
       String description,
       Double amount,
       String transferCategory,
       Long fromAccountId,
       Long toAccountId,
       LocalDate date,
       LocalDateTime createdAt,
       LocalDateTime updatedAt
) {
}
