package com.freebills.controllers.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

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
       @JsonFormat(pattern = "dd/MM/yyyy")
       LocalDate date,
       LocalDateTime createdAt,
       LocalDateTime updatedAt
) {
}
