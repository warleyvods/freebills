package com.freebills.controllers.dtos.responses;

import com.freebills.domain.Account;

import java.time.LocalDateTime;

public record TransferResponseDTO(
       Long id,
       String observation,
       String description,
       String transferCategory,
       Account from,
       Account to,
       LocalDateTime createdAt
) {
}
